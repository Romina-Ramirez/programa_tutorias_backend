package com.tutorias.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tutorias.Repository.IStudentRepository;
import com.tutorias.Repository.IUserRepository;
import com.tutorias.Repository.Enums.Role;
import com.tutorias.Repository.Model.Student;
import com.tutorias.Repository.Model.User;
import com.tutorias.Service.dto.LoginResponseDTO;
import com.tutorias.Service.dto.RegisterStudentDTO;
import com.tutorias.Service.exception.EmailSendException;
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

    @Override
    public LoginResponseDTO login(String email, String password) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No existe una cuenta asociada a este email"));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cuenta desactivada");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .role(user.getRole())
                .name(user.getName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public boolean registerStudent(RegisterStudentDTO dto) {
        if (this.userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        User user = User.builder()
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
                .orElseThrow(() -> new RuntimeException("No existe una cuenta asociada a este email"));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cuenta desactivada");
        }

        boolean send = false;

        try {
            this.emailService.sendRequestRecoveryPassword(email);
            send = true;
        } catch (MessagingException e) {
            throw new EmailSendException("No se pudo enviar el correo de cambio de contraseña.");
        }

        return send;
    }

    @Override
    public boolean changePassword(String email, String newPassword) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese email"));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cuenta desactivada");
        }

        String hashed = passwordEncoder.encode(newPassword);

        boolean updated = this.userRepository.updatePassword(email, hashed);

        if (!updated) {
            throw new RuntimeException("No se pudo cambiar la contraseña");
        }

        return updated;
    }
    
}
