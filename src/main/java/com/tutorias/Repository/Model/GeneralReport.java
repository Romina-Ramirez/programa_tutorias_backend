package com.tutorias.Repository.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "general_reports")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gere_id_seq")
    @SequenceGenerator(name = "gere_id_seq", sequenceName = "gere_id_seq", allocationSize = 1)
    @Column(name = "gere_id")
    private Integer id;

    @Column(name = "gere_observations")
    private String observation;              // La ingresa el administrador

    @Column(name = "gere_is_deleted")
    private Boolean isDeleted;
    
    @OneToOne
    @JoinColumn(name = "tuto_id", unique = true)
    private Tutor tutor;

    @OneToMany(mappedBy = "generalReport")
    //@JsonIgnore
    private List<Report> reports;
}
