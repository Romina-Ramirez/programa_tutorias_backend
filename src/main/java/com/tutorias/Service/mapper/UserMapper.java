package com.tutorias.Service.mapper;

import org.springframework.stereotype.Component;

import com.tutorias.Repository.Model.Tutor;
import com.tutorias.Repository.Model.User;
import com.tutorias.Service.dto.AdminDTO;
import com.tutorias.Service.dto.ProfileDTO;
import com.tutorias.Service.dto.StudentDTO;

@Component
public class UserMapper {

    public ProfileDTO convertToProfileDTO(User user) {
        if (user == null) return null;

        return ProfileDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .idCard(user.getIdCard())
                .role(user.getRole())
                .career(null)
                .phone(null)
                .adminId(null)
                .build();
    }

    public ProfileDTO convertToProfileDTO(User user, Tutor tutor) {
        if (user == null) return null;

        return ProfileDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .idCard(user.getIdCard())
                .role(user.getRole())
                .career(tutor != null ? tutor.getCareer() : null)
                .phone(tutor != null ? tutor.getPhone() : null)
                .adminId(tutor != null ? tutor.getAdminId() : null)
                .isActive(tutor != null ? tutor.getIsActive() : null)
                .availableSchedule(tutor != null ? tutor.getAvailableSchedule() : null)
                .build();
    }

    public StudentDTO convertToStudentDTO(User user) {
        if (user == null) return null;

        return StudentDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .build();
    }

    public AdminDTO convertToAdminDTO(User user) {
        if (user == null) return null;

        return AdminDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .idCard(user.getIdCard())
                .build();
    }
}
