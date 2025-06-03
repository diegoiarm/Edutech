package com.edutech.classroom.dto;

import com.edutech.classroom.entity.DiscountCoupon;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiscountCouponDTO {
    private Integer id;

    @NotNull(message = "El código es requerido.")
    @Size(max = 50, message = "El código no puede superar los 50 caracteres.")
    private String code;

    @NotNull(message = "La descripción es requerida.")
    @Size(max = 800, message = "La descripción no puede superar los 800 caracteres.")
    private String description;

    @NotNull(message = "El porcentaje de descuento es requerido.")
    private BigDecimal discountPercentage;

    @NotNull(message = "La fecha de inicio es requerida.")
    private LocalDate validFrom;

    @NotNull(message = "La fecha de fin es requerida.")
    private LocalDate validUntil;

    private Boolean isActive;

    public static DiscountCouponDTO fromEntity(DiscountCoupon entity) {
        DiscountCouponDTO dto = new DiscountCouponDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setDiscountPercentage(entity.getDiscountPercentage());
        dto.setValidFrom(entity.getValidFrom());
        dto.setValidUntil(entity.getValidUntil());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public DiscountCoupon toEntity() {
        DiscountCoupon entity = new DiscountCoupon();
        entity.setId(this.getId());
        entity.setCode(this.getCode());
        entity.setDescription(this.getDescription());
        entity.setDiscountPercentage(this.getDiscountPercentage());
        entity.setValidFrom(this.getValidFrom());
        entity.setValidUntil(this.getValidUntil());
        entity.setIsActive(this.getIsActive() != null ? this.getIsActive() : true);
        return entity;
    }
} 