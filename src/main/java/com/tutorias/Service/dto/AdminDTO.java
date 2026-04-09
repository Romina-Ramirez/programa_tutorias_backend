package com.tutorias.Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Datos del admin
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDTO {
    private Integer id;
    private String idCard;
    private String name;
    private String lastName;
    private String email;
}
