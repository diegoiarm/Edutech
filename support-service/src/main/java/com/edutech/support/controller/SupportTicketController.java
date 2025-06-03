package com.edutech.support.controller;

import com.edutech.common.dto.SupportTicketDTO;
import com.edutech.support.service.SupportTicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @GetMapping
    public ResponseEntity<List<SupportTicketDTO>> findAll() {
        return ResponseEntity.ok(supportTicketService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(supportTicketService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportTicketDTO>> findByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(supportTicketService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SupportTicketDTO>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(supportTicketService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<SupportTicketDTO> create(@Valid @RequestBody SupportTicketDTO dto) {
        return ResponseEntity.ok(supportTicketService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportTicketDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody SupportTicketDTO dto) {
        return ResponseEntity.ok(supportTicketService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        supportTicketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<SupportTicketDTO> resolve(@PathVariable Integer id) {
        return ResponseEntity.ok(supportTicketService.resolve(id));
    }
} 