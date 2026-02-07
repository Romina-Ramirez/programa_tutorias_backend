package com.tutorias.Repository.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import java.util.List;

import com.tutorias.Repository.Enums.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_last_name")
    private String lastName;

    @Column(name = "user_email", unique = true)
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_is_deleted")
    private Boolean isDeleted;                              // Eliminaciones lógicas de usuarios

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ForumComment> forumComments;
}
