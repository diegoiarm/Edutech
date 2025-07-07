package com.edutech.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseCategoryDTO {

    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String name;
    
    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 800, message = "La descripción no debe superar los 800 caracteres")
    private String description;
}