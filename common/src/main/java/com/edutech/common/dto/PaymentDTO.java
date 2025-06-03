package com.edutech.common.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PaymentDTO {

    @NotNull(message = "El ID del pago es obligatorio.")
    private Integer id;

    @NotNull(message = "Debe especificar el ID del usuario que realiza el pago.")
    private Integer userId;

    @NotNull(message = "Debe especificar el monto del pago.")
    @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor a cero.")
    private BigDecimal amount;

    @NotNull(message = "La fecha del pago es obligatoria.")
    private Instant paymentDate;

    @NotBlank(message = "Debe especificar el método de pago.")
    @Size(max = 100, message = "El método de pago no puede exceder los 100 caracteres.")
    private String paymentMethod;

    @NotBlank(message = "Debe especificar la institución de pago.")
    @Size(max = 200, message = "La institución de pago no puede exceder los 200 caracteres.")
    private String paymentInstitution;

    @NotBlank(message = "Debe proporcionar el ID de la transacción.")
    @Size(max = 200, message = "El ID de transacción no puede exceder los 200 caracteres.")
    private String transactionId;

    @NotBlank(message = "Debe especificar el estado del pago.")
    @Size(max = 20, message = "El estado del pago no puede exceder los 20 caracteres.")
    private String status;

}
