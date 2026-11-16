package com.tutorias.Service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tutorias.Config.AuthenticatedUser;
import com.tutorias.Repository.Enums.Role;
import com.tutorias.Repository.IStudentRepository;
import com.tutorias.Repository.IUserRepository;
import com.tutorias.Repository.Model.Student;
import com.tutorias.Repository.Model.User;
import com.tutorias.Service.dto.LoginResponseDTO;
import com.tutorias.Service.dto.RegisterStudentDTO;
import com.tutorias.Service.exception.EmailAlreadyExistsException;
import com.tutorias.Service.exception.EmailSendException;
import com.tutorias.Service.exception.IdCardAlreadyExistsException;
import com.tutorias.Service.exception.NotFoundException;

import jakarta.mail.MessagingException;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    // Política de contraseñas del sistema.
    private static final int MIN_PASSWORD_LENGTH = 8;

    private String validatePassword(String rawPassword) {
        String normalized = String.valueOf(rawPassword == null ? "" : rawPassword).trim();
        if (normalized.length() < MIN_PASSWORD_LENGTH) {
            throw new RuntimeException(
                    "La contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres.");
        }
        return normalized;
    }

    @Override
    public LoginResponseDTO login(String email, String password) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No existe una cuenta asociada a este email."));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cuenta desactivada.");
        }

        if (Boolean.FALSE.equals(user.getActivated()) || user.getPassword() == null) {
            throw new RuntimeException(
                    "Cuenta pendiente de activación. Revise el enlace enviado a su correo.");
        }

        // Control de intentos repetidos: bloqueo temporal tras varios fallos.
        loginAttemptService.checkNotLocked(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            loginAttemptService.onFailure(email);
            throw new RuntimeException("Credenciales inválidas.");
        }

        loginAttemptService.onSuccess(email);

        String accessToken = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .role(user.getRole())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public boolean registerStudent(RegisterStudentDTO dto) {
        if (dto.getIdCard() == null || dto.getIdCard().trim().isEmpty()) {
            throw new RuntimeException("El campo cédula es obligatorio.");
        }

        if (this.userRepository.existsByIdCard(dto.getIdCard())) {
            throw new IdCardAlreadyExistsException("Ya existe un usuario registrado con esta cédula.");
        }

        if (this.userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Ya existe una cuenta asociada a este email.");
        }

        String password = validatePassword(dto.getPassword());

        User user = User.builder()
                .idCard(dto.getIdCard())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(password))
                .role(Role.STUDENT)
                .isDeleted(false)
                .activated(true)
                .build();

        this.userRepository.save(user);

        Student student = Student.builder()
                .id(user.getId())
                .user(user)
                .isDeleted(false)
                .build();

        this.studentRepository.save(student);

        return true;
    }

    @Override
    public boolean requestChangePassword(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta asociada a este email."));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cuenta desactivada");
        }

        // Enlace de un solo uso: el identificador se guarda en la cuenta y
        // emitir uno nuevo invalida cualquier enlace anterior.
        String tokenId = UUID.randomUUID().toString();
        user.setSetupTokenId(tokenId);
        this.userRepository.update(user);

        try {
            String resetToken = jwtService.generatePasswordResetToken(email, tokenId);
            this.emailService.sendRequestRecoveryPassword(email, resetToken);
            return true;
        } catch (MessagingException e) {
            throw new EmailSendException("No se pudo enviar el correo de cambio de contraseña.");
        }
    }

    @Override
    public boolean changePassword(String email, String newPassword, String resetToken,
            AuthenticatedUser authenticatedUser) {
        String normalizedPassword = validatePassword(newPassword);

        User user;
        boolean viaResetToken = false;

        if (authenticatedUser != null) {
            String authenticatedEmail = authenticatedUser.email();
            String requestedEmail = String.valueOf(email == null ? "" : email).trim();

            if (!requestedEmail.isEmpty() && !authenticatedEmail.equalsIgnoreCase(requestedEmail)) {
                throw new RuntimeException("No puede cambiar la contraseña de otro usuario.");
            }

            user = this.userRepository.findByEmail(authenticatedEmail)
                    .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese email"));
        } else {
            String normalizedToken = String.valueOf(resetToken == null ? "" : resetToken).trim();
            if (normalizedToken.isEmpty() || !jwtService.validateToken(normalizedToken)) {
                throw new RuntimeException("El enlace de recuperación es inválido o expiró.");
            }

            if (!"PASSWORD_RESET".equals(jwtService.getPurposeFromToken(normalizedToken))) {
                throw new RuntimeException("El enlace de recuperación no es válido.");
            }

            String emailFromToken = jwtService.getEmailFromToken(normalizedToken);
            user = this.userRepository.findByEmail(emailFromToken)
                    .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese email"));

            // Un solo uso: el identificador del token debe coincidir con el
            // último enlace emitido para esta cuenta.
            String tokenId = jwtService.getTokenIdFromToken(normalizedToken);
            if (tokenId == null || !tokenId.equals(user.getSetupTokenId())) {
                throw new RuntimeException("El enlace de recuperación ya fue utilizado o reemplazado.");
            }
            viaResetToken = true;
        }

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cuenta desactivada");
        }

        String hashed = passwordEncoder.encode(normalizedPassword);
        boolean updated = this.userRepository.updatePassword(user.getEmail(), hashed);

        if (!updated) {
            throw new RuntimeException("No se pudo cambiar la contraseña");
        }

        if (viaResetToken) {
            user.setSetupTokenId(null);
            this.userRepository.update(user);
        }

        return true;
    }

    @Override
    public boolean activateAccount(String token, String newPassword) {
        String normalizedToken = String.valueOf(token == null ? "" : token).trim();
        if (normalizedToken.isEmpty() || !jwtService.validateToken(normalizedToken)) {
            throw new RuntimeException("El enlace de activación es inválido o expiró.");
        }

        if (!"ACCOUNT_ACTIVATION".equals(jwtService.getPurposeFromToken(normalizedToken))) {
            throw new RuntimeException("El enlace de activación no es válido.");
        }

        String emailFromToken = jwtService.getEmailFromToken(normalizedToken);
        User user = this.userRepository.findByEmail(emailFromToken)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta asociada a este enlace."));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cuenta desactivada.");
        }

        if (Boolean.TRUE.equals(user.getActivated())) {
            throw new RuntimeException("La cuenta ya fue activada. Puede iniciar sesión.");
        }

        // Un solo uso: debe ser el último enlace emitido para esta cuenta.
        String tokenId = jwtService.getTokenIdFromToken(normalizedToken);
        if (tokenId == null || !tokenId.equals(user.getSetupTokenId())) {
            throw new RuntimeException(
                    "El enlace de activación ya fue utilizado o reemplazado por uno más reciente.");
        }

        String password = validatePassword(newPassword);
        String hashed = passwordEncoder.encode(password);

        boolean updated = this.userRepository.updatePassword(user.getEmail(), hashed);
        if (!updated) {
            throw new RuntimeException("No se pudo activar la cuenta.");
        }

        user.setActivated(true);
        user.setSetupTokenId(null);
        this.userRepository.update(user);

        return true;
    }

    @Override
    public LoginResponseDTO refreshToken(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Token de refresh inválido o expirado.");
        }

        // Solo un token emitido como REFRESH puede renovar la sesión: un token
        // de recuperación o de activación no sirve para obtener acceso.
        if (!"REFRESH".equals(jwtService.getPurposeFromToken(refreshToken))) {
            throw new RuntimeException("Token de refresh inválido o expirado.");
        }

        String email = jwtService.getEmailFromToken(refreshToken);
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cuenta desactivada.");
        }

        String newAccessToken = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        String newRefreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .role(user.getRole())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
