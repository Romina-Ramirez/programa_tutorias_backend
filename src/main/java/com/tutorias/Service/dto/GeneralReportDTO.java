package com.tutorias.Service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Permite mostrar el reporte general con la lista de todos los reportes
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralReportDTO {
    private Integer tutorId;
    private String tutorName;
    private String observation;
    private List<ReportDTO> reports;
}
