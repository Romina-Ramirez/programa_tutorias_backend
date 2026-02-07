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
@Table(name = "forum_comments")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumComment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "foco_id_seq")
    @SequenceGenerator(name = "foco_id_seq", sequenceName = "foco_id_seq", allocationSize = 1)
    @Column(name = "foco_id")
    private Integer id;

    @Column(name = "foco_comment")
    private String comment;                  // Comentario sobre un foro

    @Column(name = "foco_date")
    private LocalDateTime createdAt;

    @Column(name = "foco_edited_at")
    private LocalDateTime editedAt;

    @Column(name = "foco_is_deleted")
    private Boolean isDeleted;
    
    @ManyToOne
    @JoinColumn(name = "foru_id")
    @JsonIgnore
    private Forum forum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;                       // Puede comentar tanto tutor como estudiante
}
