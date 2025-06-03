package com.edutech.classroom.dto;

import com.edutech.classroom.entity.Payment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PaymentDTO {
    private Integer id;

    @NotNull(message = "El usuario es requerido.")
    private Integer userId;

    @NotNull(message = "El monto es requerido.")
    private BigDecimal amount;

    private Instant paymentDate;

    @NotNull(message = "El método de pago es requerido.")
    @Size(max = 100, message = "El método de pago no puede superar los 100 caracteres.")
    private String paymentMethod;

    @NotNull(message = "La institución de pago es requerida.")
    @Size(max = 200, message = "La institución de pago no puede superar los 200 caracteres.")
    private String paymentInstitution;

    @NotNull(message = "El ID de transacción es requerido.")
    @Size(max = 200, message = "El ID de transacción no puede superar los 200 caracteres.")
    private String transactionId;

    @NotNull(message = "El estado es requerido.")
    @Size(max = 20, message = "El estado no puede superar los 20 caracteres.")
    private String status;

    public static PaymentDTO fromEntity(Payment entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setAmount(entity.getAmount());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setPaymentInstitution(entity.getPaymentInstitution());
        dto.setTransactionId(entity.getTransactionId());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public Payment toEntity() {
        Payment entity = new Payment();
        entity.setId(this.getId());
        // La relación con User se establecerá en el servicio
        entity.setAmount(this.getAmount());
        entity.setPaymentDate(this.getPaymentDate() != null ? this.getPaymentDate() : Instant.now());
        entity.setPaymentMethod(this.getPaymentMethod());
        entity.setPaymentInstitution(this.getPaymentInstitution());
        entity.setTransactionId(this.getTransactionId());
        entity.setStatus(this.getStatus());
        return entity;
    }
} 