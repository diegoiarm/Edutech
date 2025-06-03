package com.edutech.classroom.controller;

import com.edutech.classroom.dto.DiscountCouponDTO;
import com.edutech.classroom.dto.PaymentDTO;
import com.edutech.classroom.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    // Endpoints para Payment
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> findAllPayments() {
        return ResponseEntity.ok(service.findAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> findPaymentById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findPaymentById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDTO>> findPaymentsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.findPaymentsByUser(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentDTO>> findPaymentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.findPaymentsByStatus(status));
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody @Valid PaymentDTO dto) {
        return ResponseEntity.ok(service.createPayment(dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updatePaymentStatus(id, status));
    }

    // Endpoints para DiscountCoupon
    @GetMapping("/coupons")
    public ResponseEntity<List<DiscountCouponDTO>> findAllCoupons() {
        return ResponseEntity.ok(service.findAllCoupons());
    }

    @GetMapping("/coupons/{id}")
    public ResponseEntity<DiscountCouponDTO> findCouponById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findCouponById(id));
    }

    @GetMapping("/coupons/code/{code}")
    public ResponseEntity<DiscountCouponDTO> findCouponByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.findCouponByCode(code));
    }

    @GetMapping("/coupons/active")
    public ResponseEntity<List<DiscountCouponDTO>> findActiveCoupons() {
        return ResponseEntity.ok(service.findActiveCoupons());
    }

    @PostMapping("/coupons")
    public ResponseEntity<DiscountCouponDTO> createCoupon(@RequestBody @Valid DiscountCouponDTO dto) {
        return ResponseEntity.ok(service.createCoupon(dto));
    }

    @PutMapping("/coupons/{id}")
    public ResponseEntity<DiscountCouponDTO> updateCoupon(
            @PathVariable Integer id,
            @RequestBody @Valid DiscountCouponDTO dto) {
        return ResponseEntity.ok(service.updateCoupon(id, dto));
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Integer id) {
        service.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para calcular descuento
    @GetMapping("/calculate-discount")
    public ResponseEntity<BigDecimal> calculateDiscount(
            @RequestParam String couponCode,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(service.calculateDiscount(couponCode, amount));
    }
} 