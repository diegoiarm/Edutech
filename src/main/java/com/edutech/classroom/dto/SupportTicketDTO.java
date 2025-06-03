package com.edutech.classroom.dto;

import com.edutech.classroom.entity.SupportTicket;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class SupportTicketDTO {
    private Integer id;

    @NotNull(message = "El usuario es requerido.")
    private Integer userId;

    private Integer supportUserId;

    @NotNull(message = "El asunto es requerido.")
    @Size(max = 200, message = "El asunto no puede superar los 200 caracteres.")
    private String subject;

    @NotNull(message = "La descripción es requerida.")
    @Size(max = 800, message = "La descripción no puede superar los 800 caracteres.")
    private String description;

    @NotNull(message = "El estado es requerido.")
    @Size(max = 20, message = "El estado no puede superar los 20 caracteres.")
    private String status;

    private Instant createdAt;
    private Instant closedAt;

    public static SupportTicketDTO fromEntity(SupportTicket entity) {
        SupportTicketDTO dto = new SupportTicketDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setSupportUserId(entity.getSupportUser() != null ? entity.getSupportUser().getId() : null);
        dto.setSubject(entity.getSubject());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setClosedAt(entity.getClosedAt());
        return dto;
    }

    public SupportTicket toEntity() {
        SupportTicket entity = new SupportTicket();
        entity.setId(this.getId());
        // Las relaciones con User se establecerán en el servicio
        entity.setSubject(this.getSubject());
        entity.setDescription(this.getDescription());
        entity.setStatus(this.getStatus());
        entity.setCreatedAt(this.getCreatedAt() != null ? this.getCreatedAt() : Instant.now());
        entity.setClosedAt(this.getClosedAt());
        return entity;
    }
} 