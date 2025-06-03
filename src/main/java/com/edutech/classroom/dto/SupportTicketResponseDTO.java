package com.edutech.classroom.dto;

import com.edutech.classroom.entity.SupportTicketResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class SupportTicketResponseDTO {
    private Integer id;

    @NotNull(message = "El ID del ticket es requerido.")
    private Integer ticketId;

    @NotNull(message = "El ID del usuario es requerido.")
    private Integer userId;

    @NotNull(message = "El texto de la respuesta es requerido.")
    @Size(max = 800, message = "La respuesta no puede superar los 800 caracteres.")
    private String responseText;

    private Instant createdAt;
    private Boolean isInternal;

    public static SupportTicketResponseDTO fromEntity(SupportTicketResponse entity) {
        SupportTicketResponseDTO dto = new SupportTicketResponseDTO();
        dto.setId(entity.getId());
        dto.setTicketId(entity.getTicket().getId());
        dto.setUserId(entity.getUser().getId());
        dto.setResponseText(entity.getResponseText());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setIsInternal(entity.getIsInternal());
        return dto;
    }

    public SupportTicketResponse toEntity() {
        SupportTicketResponse entity = new SupportTicketResponse();
        entity.setId(this.getId());
        // Las relaciones con Ticket y User se establecerán en el servicio
        entity.setResponseText(this.getResponseText());
        entity.setCreatedAt(this.getCreatedAt() != null ? this.getCreatedAt() : Instant.now());
        entity.setIsInternal(this.getIsInternal() != null ? this.getIsInternal() : false);
        return entity;
    }
} 