package com.tutorias.Service.dto;



import com.tutorias.Repository.Enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *Se usará para:
 *Redirigir a una página según el rol
 *Enviar el id del usuario
**/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private Integer userId;
    private Role role;
    private String name;
    private String lastName;
}
