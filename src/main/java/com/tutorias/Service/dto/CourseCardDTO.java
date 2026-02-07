package com.tutorias.Service.dto;

import com.tutorias.Repository.Enums.CourseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Se usará para:
 * Mostrar los cursos disponibles en la página principal
 * Mostrar los cursos de tutor y estudiante.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCardDTO {
    private Integer id;
    private String name;
    private String description;
    private String schedule;
    private String subject;
    private CourseStatus status;
    private String tutor;
}
