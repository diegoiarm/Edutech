package com.edutech.payment.repository;

import com.edutech.payment.entity.DiscountCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountCouponRepository extends JpaRepository<DiscountCoupon, Integer> {
    Optional<DiscountCoupon> findByCode(String code);
    List<DiscountCoupon> findByIsActiveTrue();
    List<DiscountCoupon> findByIsActiveTrueAndValidFromBeforeAndValidUntilAfter(Instant now, Instant now2);
    boolean existsByCode(String code);
} 