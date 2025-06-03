package com.edutech.classroom.dto;

import com.edutech.classroom.entity.QuizResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class QuizResponseDTO {
    private Integer id;

    @NotNull(message = "El quiz es requerido.")
    private Integer quizId;

    @NotNull(message = "El estudiante es requerido.")
    private Integer studentId;

    @Size(max = 1, message = "La opción seleccionada no puede superar 1 carácter.")
    private String selectedOption;

    @Size(max = 800, message = "El contenido de la respuesta no puede superar los 800 caracteres.")
    private String responseContent;

    @Size(max = 800, message = "La URL de la tarea no puede superar los 800 caracteres.")
    private String assignmentUrl;

    private Instant submittedAt;

    public static QuizResponseDTO fromEntity(QuizResponse entity) {
        QuizResponseDTO dto = new QuizResponseDTO();
        dto.setId(entity.getId());
        dto.setQuizId(entity.getQuiz().getId());
        dto.setStudentId(entity.getStudent().getId());
        dto.setSelectedOption(entity.getSelectedOption());
        dto.setResponseContent(entity.getResponseContent());
        dto.setAssignmentUrl(entity.getAssignmentUrl());
        dto.setSubmittedAt(entity.getSubmittedAt());
        return dto;
    }

    public QuizResponse toEntity() {
        QuizResponse entity = new QuizResponse();
        entity.setId(this.getId());
        // Las relaciones con Quiz y Student se establecerán en el servicio
        entity.setSelectedOption(this.getSelectedOption());
        entity.setResponseContent(this.getResponseContent());
        entity.setAssignmentUrl(this.getAssignmentUrl());
        entity.setSubmittedAt(this.getSubmittedAt() != null ? this.getSubmittedAt() : Instant.now());
        return entity;
    }
} 