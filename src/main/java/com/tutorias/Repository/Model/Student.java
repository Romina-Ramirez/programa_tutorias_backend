package com.tutorias.Repository.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/*
Entidad para manejar los usuarios con Rol de estudiante 
*/
@Entity
@Table(name = "students")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @Column(name = "stud_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "stud_id")
    private User user;

    @Column(name = "stud_is_deleted")
    private Boolean isDeleted;
    
    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private List<Course> courses;

    @OneToMany(mappedBy = "student" )
    @JsonIgnore
    private List<Grade> grades;
}
