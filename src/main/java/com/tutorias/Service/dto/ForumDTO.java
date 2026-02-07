package com.tutorias.Service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Se mostrará el titulo del foro y la fecha de creación
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumDTO {
    private Integer id;
    private String title;
    private LocalDateTime createdAt;
}
