package com.edutech.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {

    private Integer id;

    @NotBlank(message = "El nombre del rol es obligatorio.")
    @Size(max = 50, message = "El nombre del rol no puede exceder los 50 caracteres.")
    private String name;

    @NotBlank(message = "La descripción del rol es obligatoria.")
    @Size(max = 800, message = "La descripción del rol no puede exceder los 800 caracteres.")
    private String description;
}
