package com.edutech.payment.controller;

import com.edutech.common.dto.DiscountCouponDTO;
import com.edutech.payment.service.DiscountCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discount-coupons")
@RequiredArgsConstructor
public class DiscountCouponController {

    private final DiscountCouponService discountCouponService;

    @GetMapping
    public ResponseEntity<List<DiscountCouponDTO>> getAll() {
        return ResponseEntity.ok(discountCouponService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountCouponDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(discountCouponService.findById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<DiscountCouponDTO> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(discountCouponService.findByCode(code));
    }

    @GetMapping("/active")
    public ResponseEntity<List<DiscountCouponDTO>> getActive() {
        return ResponseEntity.ok(discountCouponService.findActive());
    }

    @PostMapping
    public ResponseEntity<DiscountCouponDTO> create(@RequestBody DiscountCouponDTO dto) {
        return ResponseEntity.ok(discountCouponService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountCouponDTO> update(@PathVariable Integer id, @RequestBody DiscountCouponDTO dto) {
        return ResponseEntity.ok(discountCouponService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        discountCouponService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<DiscountCouponDTO> activate(@PathVariable Integer id) {
        return ResponseEntity.ok(discountCouponService.activate(id));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<DiscountCouponDTO> deactivate(@PathVariable Integer id) {
        return ResponseEntity.ok(discountCouponService.deactivate(id));
    }
} 