package com.tutorias.Service.dto;

import java.time.LocalDate;

import com.tutorias.Repository.Enums.CourseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Se usará para la vista del detalle del curso del profesor
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseTutorDetailDTO {
    private Integer id;
    private String name;
    private String schedule;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private CourseStatus status;
}
