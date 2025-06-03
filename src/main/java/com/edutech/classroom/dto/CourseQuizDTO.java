package com.edutech.classroom.dto;

import com.edutech.classroom.entity.CourseQuiz;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CourseQuizDTO {
    private Integer id;

    @NotNull(message = "El curso es requerido.")
    private Integer courseId;

    @NotNull(message = "El título es requerido.")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres.")
    private String title;

    @NotNull(message = "La descripción es requerida.")
    @Size(max = 800, message = "La descripción no puede superar los 800 caracteres.")
    private String description;

    @NotNull(message = "El tipo de quiz es requerido.")
    @Size(max = 50, message = "El tipo de quiz no puede superar los 50 caracteres.")
    private String quizType;

    private Instant createdAt;
    private List<CourseQuizQuestionDTO> questions;

    public static CourseQuizDTO fromEntity(CourseQuiz entity) {
        CourseQuizDTO dto = new CourseQuizDTO();
        dto.setId(entity.getId());
        dto.setCourseId(entity.getCourse().getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setQuizType(entity.getQuizType());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public CourseQuiz toEntity() {
        CourseQuiz entity = new CourseQuiz();
        entity.setId(this.getId());
        // La relación con Course se establecerá en el servicio
        entity.setTitle(this.getTitle());
        entity.setDescription(this.getDescription());
        entity.setQuizType(this.getQuizType());
        entity.setCreatedAt(this.getCreatedAt() != null ? this.getCreatedAt() : Instant.now());
        return entity;
    }
} 