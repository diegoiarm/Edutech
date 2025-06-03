package com.edutech.classroom.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 15, scale = 3)
    private BigDecimal amount;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @Size(max = 100)
    @NotNull
    @Column(name = "payment_method", nullable = false, length = 100)
    private String paymentMethod;

    @Size(max = 200)
    @NotNull
    @Column(name = "payment_institution", nullable = false, length = 200)
    private String paymentInstitution;

    @Size(max = 200)
    @NotNull
    @Column(name = "transaction_id", nullable = false, length = 200)
    private String transactionId;

    @Size(max = 20)
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status;

}