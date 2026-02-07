package com.tutorias.Service.mapper;

import org.springframework.stereotype.Component;

import com.tutorias.Repository.Enums.Role;
import com.tutorias.Repository.Model.User;
import com.tutorias.Service.dto.LoginResponseDTO;
import com.tutorias.Service.dto.RegisterStudentDTO;

@Component
public class AuthMapper {

    public LoginResponseDTO convertToLoginDTO(User user){
        if (user == null) return null;

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .role(user.getRole())
                .name(user.getName())
                .lastName(user.getLastName())
                .build();
    }

    public User convertToUser(RegisterStudentDTO dto){
        if (dto == null) return null;

        return User.builder()
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.STUDENT)
                .isDeleted(false)
                .build();
    }
    
}
