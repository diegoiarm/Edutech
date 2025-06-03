package com.edutech.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class CourseCommentDTO {

    @NotNull(message = "El ID del comentario es obligatorio.")
    private Integer id;

    @NotNull(message = "Debe especificar el ID del curso.")
    private Integer courseId;

    @NotNull(message = "Debe especificar el ID del usuario.")
    private Integer userId;

    @NotBlank(message = "El texto del comentario es obligatorio.")
    @Size(max = 800, message = "El comentario no puede exceder los 800 caracteres.")
    private String commentText;

    @NotNull(message = "Debe especificar una calificación.")
    @Min(value = 0, message = "La calificación mínima es 0.")
    @Max(value = 5, message = "La calificación máxima es 5.")
    private Integer rating;

    @NotNull(message = "La fecha de creación del comentario es obligatoria.")
    private Instant createdAt;
}