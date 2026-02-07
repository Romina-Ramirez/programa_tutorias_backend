package com.tutorias.Repository.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.UniqueConstraint;
 
import java.time.LocalDate;

import java.util.List;

import com.tutorias.Repository.Enums.CourseStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "courses")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cour_id_seq")
	@SequenceGenerator(name = "cour_id_seq", sequenceName = "cour_id_seq", allocationSize = 1)
    @Column(name = "cour_id")
    private Integer id;

    @Column(name = "cour_name")
    private String name;

    @Column(name = "cour_description")
    private String description;                  // Información sobre lo que se verá en el curso

    @Column(name = "cour_schedule")
    private String schedule;                     // Información del horario

    @Column(name = "cour_subject")
    private String subject;                      // Materia relacionada para filtros

    @Column(name = "cour_quota")
    private Integer quota;                       // Cupos que ofrece el curso

    @Column(name = "cour_status")
    @Enumerated(EnumType.STRING)
    private CourseStatus status;                 // Activo, En curso, Inactivo

    @Column(name = "cour_start_date")
    private LocalDate startDate;

    @Column(name = "cour_end_date")
    private LocalDate endDate;

    @Column(name = "cour_duration_minutes")
    private Integer durationMinutes;             // Duración de la clase en minutos

    @Column(name = "course_is_deleted")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "tuto_id")
    @JsonIgnore
    private Tutor tutor;

    @ManyToMany
    @JoinTable(
        name = "course_student",
        joinColumns = @JoinColumn(name = "cour_id"),
        inverseJoinColumns = @JoinColumn(name = "stud_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"cour_id","stud_id"})
    )
    @JsonIgnore
    private List<Student> students;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Grade> grades;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Forum> forums;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Report> reports;
}