package com.edutech.common.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CourseDTO {

    private Integer id;

    @NotBlank(message = "El título del curso es obligatorio.")
    @Size(max = 200, message = "El título no puede exceder los 200 caracteres.")
    private String title;

    @NotBlank(message = "La descripción del curso es obligatoria.")
    @Size(max = 800, message = "La descripción no puede exceder los 800 caracteres.")
    private String description;

    @NotNull(message = "Debe especificar el ID de la categoría.")
    private Integer categoryId;

    @NotNull(message = "Debe especificar el ID del encargado del curso.")
    private Integer managerId;

    @NotNull(message = "Debe especificar el ID del instructor del curso.")
    private Integer instructorId;

    @NotNull(message = "La fecha de publicación es obligatoria.")
    private LocalDate publishDate;

    @NotNull(message = "El precio del curso es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0.")
    private BigDecimal price;

    @NotBlank(message = "La imagen del curso es obligatoria.")
    private String image;

    @NotBlank(message = "El estado del curso es obligatorio.")
    @Size(max = 50, message = "El estado no puede exceder los 50 caracteres.")
    private String status;
}
