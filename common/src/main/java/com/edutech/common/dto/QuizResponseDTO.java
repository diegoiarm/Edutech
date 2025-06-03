package com.edutech.common.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class QuizResponseDTO {

    @NotNull(message = "El ID de la respuesta es obligatorio.")
    private Integer id;

    @NotNull(message = "Debe especificar el ID del quiz.")
    private Integer quizId;

    @NotNull(message = "Debe especificar el ID del estudiante.")
    private Integer studentId;

    @Size(max = 1, message = "La opción seleccionada debe ser solo una letra.")
    private String selectedOption;

    @Size(max = 800, message = "El contenido de la respuesta no puede exceder los 800 caracteres.")
    private String responseContent;

    @Size(max = 800, message = "La URL de entrega no puede exceder los 800 caracteres.")
    private String assignmentUrl;

    @NotNull(message = "La fecha de envío de la respuesta es obligatoria.")
    private Instant submittedAt;

}
