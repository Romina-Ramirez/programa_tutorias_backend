package com.tutorias.Repository.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tutors")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tutor {

    @Id
    @Column(name = "tuto_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "tuto_id")
    private User user;

    @Column(name = "tuto_phone")
    private String phone;                          // Información únicamente para el administrador

    @Column(name = "tuto_career")
    private String career;

    @Column(name = "tuto_is_deleted")
    private Boolean isDeleted;

    @Column(name = "tuto_is_Active")
    private Boolean isActive;                      // Tutor verificado

    @Column(name = "tuto_available_schedule")
    private String availableSchedule;              // En qué horario podría dar clases

    @Column(name = "tuto_minutes_completed")
    private Integer minutesCompleted;              // Se acumula en minutos

    @Column(name = "tuto_admin_id")
    private Integer adminId;                      // Rol de administrador

    @OneToMany(mappedBy = "tutor")
    //@JsonIgnore
    private List<Course> courses;

    @OneToOne(mappedBy = "tutor")
    @JsonIgnore
    private GeneralReport generalReport;
}
