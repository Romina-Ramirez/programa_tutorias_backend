package com.tutorias.Service;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tutorias.Repository.ITutorRepository;
import com.tutorias.Repository.IUserRepository;
import com.tutorias.Repository.Enums.Role;
import com.tutorias.Repository.Model.User;
import com.tutorias.Service.dto.AdminDTO;
import com.tutorias.Service.exception.EmailSendException;
import com.tutorias.Service.exception.NotFoundException;
import com.tutorias.Service.mapper.UserMapper;

import jakarta.mail.MessagingException;

@Service
public class SuperAdminServiceImpl implements ISuperAdminService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ITutorRepository tutorRepository;

    @Autowired 
    private UserMapper userMapper;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminDTO createAdmin(AdminDTO dto) {

        if (this.userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email.");
        }

        User user = User.builder()
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(null)
                .role(Role.ADMIN)
                .isDeleted(false)
                .build();

        this.userRepository.save(user);

        sendEmailNewAdmin(user.getId());

        return this.userMapper.convertToAdminDTO(user);
    }

    @Override
    public boolean sendEmailNewAdmin(Integer adminId) {

        User user = this.userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin no encontrado"));

        String password = generateRandomPassword(8);
        String hashed = this.passwordEncoder.encode(password);

        boolean updated = this.userRepository.updatePassword(user.getEmail(), hashed);

        if (!updated) {
            throw new RuntimeException("No se pudo cambiar la contraseña");
        }

        try {
            this.emailService.sendnewTutorOrAdmin(user.getEmail(),password,user.getRole().name());
            return true;

        } catch (MessagingException e) {
            throw new EmailSendException("No se pudo enviar el correo al admin.");
        }
    }

    @Override
    public List<AdminDTO> listAdmins() {
        return this.userRepository.findAllAdmins().stream()
                .map(userMapper::convertToAdminDTO)
                .toList();
    }

    @Override
    public AdminDTO updateAdmin(Integer adminId, AdminDTO dto) {

        User user = this.userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin no encontrado"));

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());

        this.userRepository.update(user);

        return this.userMapper.convertToAdminDTO(user);
    }

    @Override
    public boolean deleteAdmin(Integer adminId, Integer newAdminId) {
        this.tutorRepository.reassignAdmin(adminId, newAdminId);
        return this.userRepository.softDelete(adminId);
        
    }

    public static String generateRandomPassword(int length) {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
