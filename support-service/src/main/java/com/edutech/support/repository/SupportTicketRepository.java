package com.edutech.support.repository;

import com.edutech.support.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Integer> {
    List<SupportTicket> findByUserId(Integer userId);
    List<SupportTicket> findByStatus(String status);
    List<SupportTicket> findByUserIdAndStatus(Integer userId, String status);
    Optional<SupportTicket> findByIdAndUserId(Integer id, Integer userId);
} 