package com.tutorias.Service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Resultado del job de actualización de estados
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseStatusJobDTO {
    private int updatedToInProgress;
    private int updatedToInactive;
    private LocalDate executionDate;
}
