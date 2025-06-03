package com.edutech.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseContentDTO {

    @NotNull(message = "El ID del contenido es obligatorio.")
    private Integer id;

    @NotNull(message = "Debe especificar el ID del curso al que pertenece el contenido.")
    private Integer courseId;

    @NotBlank(message = "El título del contenido es obligatorio.")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres.")
    private String title;

    @NotBlank(message = "El tipo de contenido es obligatorio.")
    @Size(max = 50, message = "El tipo de contenido no puede superar los 50 caracteres.")
    private String contentType;

    @NotBlank(message = "La URL del contenido es obligatoria.")
    @Size(max = 800, message = "La URL no puede superar los 800 caracteres.")
    private String url;

    @NotNull(message = "Debe especificar el orden del contenido.")
    @Min(value = 1, message = "El orden debe ser un número positivo mayor a 0.")
    private Integer orderIndex;
}
