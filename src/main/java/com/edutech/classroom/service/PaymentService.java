package com.edutech.classroom.service;

import com.edutech.classroom.dto.DiscountCouponDTO;
import com.edutech.classroom.dto.PaymentDTO;
import com.edutech.classroom.entity.DiscountCoupon;
import com.edutech.classroom.entity.Payment;
import com.edutech.classroom.entity.User;
import com.edutech.classroom.exception.ResourceNotFoundException;
import com.edutech.classroom.exception.ValidationException;
import com.edutech.classroom.repository.DiscountCouponRepository;
import com.edutech.classroom.repository.PaymentRepository;
import com.edutech.classroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DiscountCouponRepository couponRepository;
    private final UserRepository userRepository;

    // Métodos para Payment
    public List<PaymentDTO> findAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentDTO::fromEntity)
                .toList();
    }

    public PaymentDTO findPaymentById(Integer id) {
        return PaymentDTO.fromEntity(paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado.")));
    }

    public List<PaymentDTO> findPaymentsByUser(Integer userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(PaymentDTO::fromEntity)
                .toList();
    }

    public List<PaymentDTO> findPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status).stream()
                .map(PaymentDTO::fromEntity)
                .toList();
    }

    @Transactional
    public PaymentDTO createPayment(PaymentDTO dto) {
        // Validar que el usuario existe y está activo
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
        if (!user.getIsActive()) {
            throw new ValidationException("El usuario no está activo.");
        }

        // Validar que no existe un pago con el mismo ID de transacción
        if (paymentRepository.existsByTransactionId(dto.getTransactionId())) {
            throw new ValidationException("Ya existe un pago con ese ID de transacción.");
        }

        // Validar que el monto es positivo
        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El monto debe ser mayor a cero.");
        }

        Payment payment = dto.toEntity();
        payment.setUser(user);

        return PaymentDTO.fromEntity(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentDTO updatePaymentStatus(Integer id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado."));

        payment.setStatus(status);
        return PaymentDTO.fromEntity(paymentRepository.save(payment));
    }

    // Métodos para DiscountCoupon
    public List<DiscountCouponDTO> findAllCoupons() {
        return couponRepository.findAll().stream()
                .map(DiscountCouponDTO::fromEntity)
                .toList();
    }

    public DiscountCouponDTO findCouponById(Integer id) {
        return DiscountCouponDTO.fromEntity(couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado.")));
    }

    public DiscountCouponDTO findCouponByCode(String code) {
        return DiscountCouponDTO.fromEntity(couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado.")));
    }

    public List<DiscountCouponDTO> findActiveCoupons() {
        return couponRepository.findByIsActive(true).stream()
                .map(DiscountCouponDTO::fromEntity)
                .toList();
    }

    @Transactional
    public DiscountCouponDTO createCoupon(DiscountCouponDTO dto) {
        // Validar que no existe un cupón con el mismo código
        if (couponRepository.existsByCode(dto.getCode())) {
            throw new ValidationException("Ya existe un cupón con ese código.");
        }

        // Validar que las fechas son válidas
        if (dto.getValidFrom().isAfter(dto.getValidUntil())) {
            throw new ValidationException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        // Validar que el porcentaje de descuento está entre 0 y 100
        if (dto.getDiscountPercentage().compareTo(BigDecimal.ZERO) < 0 ||
            dto.getDiscountPercentage().compareTo(new BigDecimal("100")) > 0) {
            throw new ValidationException("El porcentaje de descuento debe estar entre 0 y 100.");
        }

        return DiscountCouponDTO.fromEntity(couponRepository.save(dto.toEntity()));
    }

    @Transactional
    public DiscountCouponDTO updateCoupon(Integer id, DiscountCouponDTO dto) {
        DiscountCoupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado."));

        // Validar que no existe otro cupón con el mismo código
        if (!existingCoupon.getCode().equals(dto.getCode()) &&
            couponRepository.existsByCode(dto.getCode())) {
            throw new ValidationException("Ya existe un cupón con ese código.");
        }

        // Validar que las fechas son válidas
        if (dto.getValidFrom().isAfter(dto.getValidUntil())) {
            throw new ValidationException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        // Validar que el porcentaje de descuento está entre 0 y 100
        if (dto.getDiscountPercentage().compareTo(BigDecimal.ZERO) < 0 ||
            dto.getDiscountPercentage().compareTo(new BigDecimal("100")) > 0) {
            throw new ValidationException("El porcentaje de descuento debe estar entre 0 y 100.");
        }

        DiscountCoupon coupon = dto.toEntity();
        coupon.setId(id);

        return DiscountCouponDTO.fromEntity(couponRepository.save(coupon));
    }

    @Transactional
    public void deleteCoupon(Integer id) {
        if (!couponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cupón no encontrado.");
        }
        couponRepository.deleteById(id);
    }

    // Método para calcular el descuento
    public BigDecimal calculateDiscount(String couponCode, BigDecimal originalAmount) {
        DiscountCoupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado."));

        // Validar que el cupón está activo
        if (!coupon.getIsActive()) {
            throw new ValidationException("El cupón no está activo.");
        }

        // Validar que el cupón está vigente
        LocalDate today = LocalDate.now();
        if (today.isBefore(coupon.getValidFrom()) || today.isAfter(coupon.getValidUntil())) {
            throw new ValidationException("El cupón no está vigente.");
        }

        // Calcular el descuento
        BigDecimal discountAmount = originalAmount.multiply(coupon.getDiscountPercentage())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return originalAmount.subtract(discountAmount);
    }
} 