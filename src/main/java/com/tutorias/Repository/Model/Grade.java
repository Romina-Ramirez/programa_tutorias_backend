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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "grades")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grad_id_seq")
    @SequenceGenerator(name = "grad_id_seq", sequenceName = "grad_id_seq", allocationSize = 1)
    @Column(name = "grad_id")
    private Integer id;

    @Column(name = "grad_activity")
    private String activity;                          // Actividad en la que fue puesta la nota

    @Column(name = "grad_qualification")
    private Double qualification;                     // Calificación en la actividad

    @Column(name = "grad_observations") 
    private String observations;                      // Observaciones (opcional)

    @Column(name = "grad_is_deleted")
    private Boolean isDeleted;
    
    @ManyToOne
    @JoinColumn(name = "stud_id")
    @JsonIgnore
    private Student student;

    @ManyToOne
    @JoinColumn(name = "cour_id")
    @JsonIgnore
    private Course course;
}
