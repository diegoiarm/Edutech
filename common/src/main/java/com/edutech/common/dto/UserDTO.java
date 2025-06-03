package com.edutech.common.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {

    private Integer id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres.")
    private String lastName;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "Debe ingresar un correo electrónico válido.")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String passwordHash;

    @NotNull(message = "Debe especificar el rol del usuario.")
    private Integer roleId;

    @NotNull(message = "Debe indicar si el usuario está activo.")
    private Boolean isActive;

    @NotNull(message = "La fecha de creación es obligatoria.")
    private Instant createdAt;

    @NotNull(message = "La fecha de última actualización es obligatoria.")
    private Instant updatedAt;

}
