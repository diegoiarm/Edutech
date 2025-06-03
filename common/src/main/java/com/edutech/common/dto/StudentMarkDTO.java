package com.edutech.common.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class StudentMarkDTO {

    @NotNull(message = "El ID de la calificación es obligatorio.")
    private Integer id;

    @NotNull(message = "Debe especificar el ID del quiz.")
    private Integer quizId;

    @NotNull(message = "Debe especificar el ID del estudiante.")
    private Integer studentId;

    @NotNull(message = "Debe ingresar la nota del estudiante.")
    @DecimalMin(value = "0.0", inclusive = true, message = "La nota debe ser mayor o igual a 0.")
    @DecimalMax(value = "100.0", inclusive = true, message = "La nota no puede superar 100.")
    private BigDecimal mark;

    @Size(max = 800, message = "Los comentarios no pueden superar los 800 caracteres.")
    private String comments;

    @NotNull(message = "Debe registrar la fecha en que se asignó la nota.")
    private Instant gradedAt;

}
