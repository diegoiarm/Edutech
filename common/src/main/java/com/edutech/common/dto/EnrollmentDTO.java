package com.edutech.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class EnrollmentDTO {

    @NotNull(message = "El ID de la matrícula es obligatorio.")
    private Integer id;

    @NotNull(message = "Debe especificar el ID del estudiante.")
    private Integer studentId;

    @NotNull(message = "Debe especificar el ID del curso.")
    private Integer courseId;

    @NotNull(message = "La fecha de inscripción es obligatoria.")
    private Instant enrolledAt;

    @NotBlank(message = "El estado de la matrícula es obligatorio.")
    @Size(max = 20, message = "El estado no puede exceder los 20 caracteres.")
    private String status;

}
