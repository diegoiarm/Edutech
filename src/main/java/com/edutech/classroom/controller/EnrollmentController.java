package com.edutech.classroom.controller;

import com.edutech.classroom.dto.EnrollmentDTO;
import com.edutech.classroom.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService service;

    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> findByStudent(@PathVariable Integer studentId) {
        return ResponseEntity.ok(service.findByStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> findByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(service.findByCourse(courseId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EnrollmentDTO>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Long> countByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(service.countByCourse(courseId));
    }

    @PostMapping
    public ResponseEntity<EnrollmentDTO> create(@RequestBody @Valid EnrollmentDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> update(@PathVariable Integer id, @RequestBody @Valid EnrollmentDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EnrollmentDTO> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
} 