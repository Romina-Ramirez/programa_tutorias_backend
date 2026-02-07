package com.tutorias.Service.mapper;

import org.springframework.stereotype.Component;

import com.tutorias.Repository.Model.Student;
import com.tutorias.Repository.Model.Tutor;
import com.tutorias.Repository.Model.User;
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
                .role(user.getRole())
                .career(tutor != null ? tutor.getCareer() : null)
                .phone(tutor != null ? tutor.getPhone() : null)
                .adminId(tutor != null ? tutor.getAdminId() : null)
                .build();
    }

    public ProfileDTO convertToProfileDTO(User user, Student student) {
        return convertToProfileDTO(user);
    }

    public StudentDTO convertToStudentDTO(User user) {
        if (user == null) return null;

        return StudentDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .build();
    }
}
