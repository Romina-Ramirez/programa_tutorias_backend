package com.tutorias.Service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Permitirá mostrar la lista de todas las calificaciones del estudiante
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentGradesDTO {
    private Integer courseId;
    private String courseName;
    private List<GradeDTO> grades;
}
