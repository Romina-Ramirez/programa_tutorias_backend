package com.tutorias.Service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Permite mostrar los datos de un reporte
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Integer id;
    private LocalDateTime createdAt;
    private String activityDescription;
    private Integer minutesCompleted;
}
