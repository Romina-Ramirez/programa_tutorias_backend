package com.tutorias.Service;

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

    @Override
    public LoginResponseDTO login(String email, String password) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No existe una cuenta asociada a este email."));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cuenta desactivada.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas.");
        }

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

        User user = User.builder()
                .idCard(dto.getIdCard())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.STUDENT)
                .isDeleted(false)
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

        try {
            String resetToken = jwtService.generatePasswordResetToken(email);
            this.emailService.sendRequestRecoveryPassword(email, resetToken);
            return true;
        } catch (MessagingException e) {
            throw new EmailSendException("No se pudo enviar el correo de cambio de contraseña.");
        }
    }

    @Override
    public boolean changePassword(String email, String newPassword, String resetToken,
            AuthenticatedUser authenticatedUser) {
        String normalizedPassword = String.valueOf(newPassword == null ? "" : newPassword).trim();
        if (normalizedPassword.isEmpty()) {
            throw new RuntimeException("La nueva contraseña es obligatoria.");
        }

        User user;

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
        }

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cuenta desactivada");
        }

        String hashed = passwordEncoder.encode(normalizedPassword);
        boolean updated = this.userRepository.updatePassword(user.getEmail(), hashed);

        if (!updated) {
            throw new RuntimeException("No se pudo cambiar la contraseña");
        }

        return true;
    }

    @Override
    public LoginResponseDTO refreshToken(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
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
