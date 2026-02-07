package com.tutorias.Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Estos son los datos que vienen desde el registro
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterStudentDTO {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String career;
    private String phone;
}
