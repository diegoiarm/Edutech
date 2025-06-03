package com.edutech.classroom.controller;

import com.edutech.classroom.dto.SupportTicketDTO;
import com.edutech.classroom.dto.SupportTicketResponseDTO;
import com.edutech.classroom.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class SupportController {
    private final SupportService supportService;

    // Endpoints para tickets de soporte
    @GetMapping("/tickets")
    public ResponseEntity<List<SupportTicketDTO>> getAllTickets() {
        return ResponseEntity.ok(supportService.findAllTickets());
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<SupportTicketDTO> getTicketById(@PathVariable Integer id) {
        return ResponseEntity.ok(supportService.findTicketById(id));
    }

    @GetMapping("/tickets/user/{userId}")
    public ResponseEntity<List<SupportTicketDTO>> getTicketsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(supportService.findTicketsByUser(userId));
    }

    @GetMapping("/tickets/support-user/{supportUserId}")
    public ResponseEntity<List<SupportTicketDTO>> getTicketsBySupportUser(@PathVariable Integer supportUserId) {
        return ResponseEntity.ok(supportService.findTicketsBySupportUser(supportUserId));
    }

    @GetMapping("/tickets/status/{status}")
    public ResponseEntity<List<SupportTicketDTO>> getTicketsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(supportService.findTicketsByStatus(status));
    }

    @PostMapping("/tickets")
    public ResponseEntity<SupportTicketDTO> createTicket(@RequestBody SupportTicketDTO ticketDTO) {
        return ResponseEntity.ok(supportService.createTicket(ticketDTO));
    }

    @PutMapping("/tickets/{ticketId}/assign/{supportUserId}")
    public ResponseEntity<SupportTicketDTO> assignSupportUser(
            @PathVariable Integer ticketId,
            @PathVariable Integer supportUserId) {
        return ResponseEntity.ok(supportService.assignSupportUser(ticketId, supportUserId));
    }

    @PutMapping("/tickets/{ticketId}/status/{status}")
    public ResponseEntity<SupportTicketDTO> updateTicketStatus(
            @PathVariable Integer ticketId,
            @PathVariable String status) {
        return ResponseEntity.ok(supportService.updateTicketStatus(ticketId, status));
    }

    // Endpoints para respuestas de tickets
    @GetMapping("/tickets/{ticketId}/responses")
    public ResponseEntity<List<SupportTicketResponseDTO>> getResponsesByTicket(@PathVariable Integer ticketId) {
        return ResponseEntity.ok(supportService.findResponsesByTicket(ticketId));
    }

    @GetMapping("/responses/user/{userId}")
    public ResponseEntity<List<SupportTicketResponseDTO>> getResponsesByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(supportService.findResponsesByUser(userId));
    }

    @PostMapping("/responses")
    public ResponseEntity<SupportTicketResponseDTO> createResponse(@RequestBody SupportTicketResponseDTO responseDTO) {
        return ResponseEntity.ok(supportService.createResponse(responseDTO));
    }

    @DeleteMapping("/responses/{responseId}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Integer responseId) {
        supportService.deleteResponse(responseId);
        return ResponseEntity.noContent().build();
    }
} 