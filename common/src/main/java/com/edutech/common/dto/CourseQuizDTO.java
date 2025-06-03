package com.edutech.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class CourseQuizDTO {

    @NotNull(message = "El ID del quiz es obligatorio.")
    private Integer id;

    @NotNull(message = "Debe especificar el ID del curso al que pertenece el quiz.")
    private Integer courseId;

    @NotBlank(message = "El título del quiz es obligatorio.")
    @Size(max = 200, message = "El título no puede exceder los 200 caracteres.")
    private String title;

    @NotBlank(message = "La descripción del quiz es obligatoria.")
    @Size(max = 800, message = "La descripción no puede exceder los 800 caracteres.")
    private String description;

    @NotBlank(message = "El tipo de quiz es obligatorio.")
    @Size(max = 50, message = "El tipo de quiz no puede exceder los 50 caracteres.")
    private String quizType;

    @NotNull(message = "La fecha de creación es obligatoria.")
    private Instant createdAt;
}
