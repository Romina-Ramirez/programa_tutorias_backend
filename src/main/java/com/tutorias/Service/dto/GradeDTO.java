package com.tutorias.Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Son los datos para mostrar en la fila del listado de calificaciones del estudiante
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeDTO {
    private Integer id;
    private String activity;
    private Double qualification;
    private String observations;
}
