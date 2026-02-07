package com.tutorias.Service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Datos para mostrar los comentarios del foro
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumCommentDTO {
    private Integer id;
    private String comment;
    private String authorName;
    private LocalDateTime createdAt;
}
