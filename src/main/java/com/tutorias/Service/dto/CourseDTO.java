package com.tutorias.Service.dto;

import java.time.LocalDate;

import com.tutorias.Repository.Enums.CourseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Son los datos que se utilizarán para el registro del curso
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Integer id;
    private String name;
    private String description;
    private String schedule;
    private String subject;
    private Integer quota;
    private CourseStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer tutorId;
    private Integer durationMinutes;
}
