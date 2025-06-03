package com.edutech.classroom.dto;

import com.edutech.classroom.entity.StudentMark;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class StudentMarkDTO {
    private Integer id;

    @NotNull(message = "El quiz es requerido.")
    private Integer quizId;

    @NotNull(message = "El estudiante es requerido.")
    private Integer studentId;

    @NotNull(message = "La calificación es requerida.")
    private BigDecimal mark;

    @Size(max = 800, message = "Los comentarios no pueden superar los 800 caracteres.")
    private String comments;

    private Instant gradedAt;

    public static StudentMarkDTO fromEntity(StudentMark entity) {
        StudentMarkDTO dto = new StudentMarkDTO();
        dto.setId(entity.getId());
        dto.setQuizId(entity.getQuiz().getId());
        dto.setStudentId(entity.getStudent().getId());
        dto.setMark(entity.getMark());
        dto.setComments(entity.getComments());
        dto.setGradedAt(entity.getGradedAt());
        return dto;
    }

    public StudentMark toEntity() {
        StudentMark entity = new StudentMark();
        entity.setId(this.getId());
        // Las relaciones con Quiz y Student se establecerán en el servicio
        entity.setMark(this.getMark());
        entity.setComments(this.getComments());
        entity.setGradedAt(this.getGradedAt() != null ? this.getGradedAt() : Instant.now());
        return entity;
    }
} 