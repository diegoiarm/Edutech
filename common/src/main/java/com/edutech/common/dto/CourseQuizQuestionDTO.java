package com.edutech.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class CourseQuizQuestionDTO {

    @NotNull(message = "El ID de la pregunta es obligatorio.")
    private Integer id;

    private Integer quizId;

    @Size(max = 800, message = "El texto de la pregunta no puede exceder los 800 caracteres.")
    private String questionText;

    @Size(max = 800, message = "La opción A no puede exceder los 800 caracteres.")
    private String optionA;

    @Size(max = 800, message = "La opción B no puede exceder los 800 caracteres.")
    private String optionB;

    @Size(max = 800, message = "La opción C no puede exceder los 800 caracteres.")
    private String optionC;

    @Size(max = 800, message = "La opción D no puede exceder los 800 caracteres.")
    private String optionD;

    @Size(max = 800, message = "La opción E no puede exceder los 800 caracteres.")
    private String optionE;

    @Size(max = 800, message = "La respuesta correcta no puede exceder los 800 caracteres.")
    private String correctAnswer;

    @Size(max = 1, message = "La opción correcta debe ser solo una letra de la A a la E.")
    private String correctOption;

    @NotNull(message = "Debe especificar el orden de la pregunta.")
    @Min(value = 1, message = "El orden debe ser un número entero ositivo mayor o igual a 1.")
    private Integer orderIndex;

    @NotNull(message = "La fecha de creación es obligatoria.")
    private Instant createdAt;

}
