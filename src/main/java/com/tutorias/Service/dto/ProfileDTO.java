package com.tutorias.Service.dto;

import com.tutorias.Repository.Enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Son los datos que se mostrarán del estudiante y profesor
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    private Integer userId;
    private String name;
    private String lastName;
    private String email;
    private Role role;

    // Datos del profesor
    private String career;
    private String phone;
    private Integer adminId;
}
