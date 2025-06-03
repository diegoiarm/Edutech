package com.edutech.classroom.dto;

import com.edutech.classroom.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {
    private Integer id;

    @NotNull(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String firstName;

    @NotNull(message = "El apellido no puede estar vacío.")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres.")
    private String lastName;

    @NotNull(message = "El email no puede estar vacío.")
    @Email(message = "El formato del email no es válido.")
    @Size(max = 255, message = "El email no puede superar los 255 caracteres.")
    private String email;

    @NotNull(message = "La contraseña no puede estar vacía.")
    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres.")
    private String passwordHash;

    @NotNull(message = "El rol no puede estar vacío.")
    private String role;

    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    public static UserDTO fromEntity(User entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPasswordHash(entity.getPasswordHash());
        dto.setRole(entity.getRole().getName());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public User toEntity() {
        User entity = new User();
        entity.setId(this.getId());
        entity.setFirstName(this.getFirstName());
        entity.setLastName(this.getLastName());
        entity.setEmail(this.getEmail());
        entity.setPasswordHash(this.getPasswordHash());
        entity.setIsActive(this.getIsActive() != null ? this.getIsActive() : true);
        entity.setCreatedAt(this.getCreatedAt() != null ? this.getCreatedAt() : Instant.now());
        entity.setUpdatedAt(this.getUpdatedAt() != null ? this.getUpdatedAt() : Instant.now());
        return entity;
    }
} 