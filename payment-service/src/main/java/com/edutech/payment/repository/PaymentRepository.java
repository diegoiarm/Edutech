package com.edutech.payment.repository;

import com.edutech.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByUserId(Integer userId);
    List<Payment> findByStatus(String status);
    Optional<Payment> findByUserIdAndStatus(Integer userId, String status);
    boolean existsByUserIdAndStatus(Integer userId, String status);
} 