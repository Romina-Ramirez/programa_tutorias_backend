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

    @Column(name = "stud_is_deleted")
    private Boolean isDeleted;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "stud_id")
    private User user;

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private List<Course> courses;

    @OneToMany(mappedBy = "student" )
    @JsonIgnore
    private List<Grade> grades;
}
