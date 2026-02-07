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
import jakarta.persistence.OneToMany;
 
import java.time.LocalDateTime;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "forums")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "foru_id_seq")
    @SequenceGenerator(name = "foru_id_seq", sequenceName = "foru_id_seq", allocationSize = 1)
    @Column(name = "foru_id")
    private Integer id;

    @Column(name = "foru_title")
    private String title;                         // Información sobre el tema del foro

    @Column(name = "foru_created_at")
    private LocalDateTime createdAt;

    @Column(name = "foru_edited_at")
    private LocalDateTime editedAt;

    @Column(name = "foru_is_deleted")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "cour_id")
    @JsonIgnore
    private Course course;

    @OneToMany(mappedBy = "forum")
    @JsonIgnore
    private List<ForumComment> comments;
}
