package com.edutech.classroom.repository;

import com.edutech.classroom.entity.DiscountCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountCouponRepository extends JpaRepository<DiscountCoupon, Integer> {
    Optional<DiscountCoupon> findByCode(String code);
    List<DiscountCoupon> findByIsActive(Boolean isActive);
    List<DiscountCoupon> findByValidFromLessThanEqualAndValidUntilGreaterThanEqualAndIsActiveTrue(
            LocalDate date, LocalDate sameDate);
    boolean existsByCode(String code);
} 