package com.edutech.classroom.repository;

import com.edutech.classroom.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByUserId(Integer userId);
    List<Payment> findByStatus(String status);
    boolean existsByTransactionId(String transactionId);
    List<Payment> findByUserIdAndStatus(Integer userId, String status);
} 