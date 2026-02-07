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

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "reports")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repo_id_seq")
    @SequenceGenerator(name = "repo_id_seq", sequenceName = "repo_id_seq", allocationSize = 1)
    @Column(name = "repo_id")
    private Integer id;

    @Column(name = "repo_created")
    private LocalDateTime createdAt;

    @Column(name = "repo_activity_description")
    private String activityDescription;                     // Información sobre la clase que dió o la actividad que realizó el tutor

    @Column(name = "repo_minutes_completed")
    private Integer minutesCompleted;                       // Cuánto tiempo llevó la clase
    
    @ManyToOne
    @JoinColumn(name = "cour_id")
    @JsonIgnore
    private Course course;

    @ManyToOne
    @JoinColumn(name = "gere_id")
    @JsonIgnore
    private GeneralReport generalReport;
}
