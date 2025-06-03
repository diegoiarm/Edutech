package com.edutech.classroom.dto;

import com.edutech.classroom.entity.CourseComment;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class CourseCommentDTO {
    private Integer id;

    @NotNull(message = "El ID del curso es requerido.")
    private Integer courseId;

    @NotNull(message = "El ID del usuario es requerido.")
    private Integer userId;

    @NotNull(message = "El texto del comentario es requerido.")
    @Size(max = 800, message = "El comentario no puede superar los 800 caracteres.")
    private String commentText;

    @NotNull(message = "La calificación es requerida.")
    @Min(value = 1, message = "La calificación debe ser al menos 1.")
    @Max(value = 5, message = "La calificación no puede ser mayor a 5.")
    private Integer rating;

    private Instant createdAt;

    public static CourseCommentDTO fromEntity(CourseComment entity) {
        CourseCommentDTO dto = new CourseCommentDTO();
        dto.setId(entity.getId());
        dto.setCourseId(entity.getCourse().getId());
        dto.setUserId(entity.getUser().getId());
        dto.setCommentText(entity.getCommentText());
        dto.setRating(entity.getRating());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public CourseComment toEntity() {
        CourseComment entity = new CourseComment();
        entity.setId(this.getId());
        // Las relaciones con Course y User se establecerán en el servicio
        entity.setCommentText(this.getCommentText());
        entity.setRating(this.getRating());
        entity.setCreatedAt(this.getCreatedAt() != null ? this.getCreatedAt() : Instant.now());
        return entity;
    }
} 