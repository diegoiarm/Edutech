package com.edutech.classroom.repository;

import com.edutech.classroom.entity.SupportTicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketResponseRepository extends JpaRepository<SupportTicketResponse, Integer> {
    List<SupportTicketResponse> findByTicketId(Integer ticketId);
    List<SupportTicketResponse> findByUserId(Integer userId);
    List<SupportTicketResponse> findByTicketIdAndIsInternal(Integer ticketId, Boolean isInternal);
    List<SupportTicketResponse> findByTicketIdOrderByCreatedAtAsc(Integer ticketId);
} 