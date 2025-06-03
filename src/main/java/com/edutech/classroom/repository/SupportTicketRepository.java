package com.edutech.classroom.repository;

import com.edutech.classroom.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Integer> {
    List<SupportTicket> findByUserId(Integer userId);
    List<SupportTicket> findBySupportUserId(Integer supportUserId);
    List<SupportTicket> findByStatus(String status);
    List<SupportTicket> findByUserIdAndStatus(Integer userId, String status);
    List<SupportTicket> findBySupportUserIdAndStatus(Integer supportUserId, String status);
} 