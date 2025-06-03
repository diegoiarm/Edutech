package com.edutech.classroom.controller;

import com.edutech.classroom.dto.CourseDTO;
import com.edutech.classroom.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseDTO>> findByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(service.findByCategory(categoryId));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<CourseDTO>> findByManager(@PathVariable Integer managerId) {
        return ResponseEntity.ok(service.findByManager(managerId));
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseDTO>> findByInstructor(@PathVariable Integer instructorId) {
        return ResponseEntity.ok(service.findByInstructor(instructorId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CourseDTO>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<CourseDTO> create(@RequestBody @Valid CourseDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable Integer id, @RequestBody @Valid CourseDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CourseDTO> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
} 