package com.edutech.marks.controller;

import com.edutech.common.dto.StudentMarkDTO;
import com.edutech.marks.service.StudentMarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks")
@RequiredArgsConstructor
public class StudentMarkController {

    private final StudentMarkService studentMarkService;

    @GetMapping
    public ResponseEntity<List<StudentMarkDTO>> findAll() {
        return ResponseEntity.ok(studentMarkService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentMarkDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentMarkService.findById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentMarkDTO>> findByStudentId(@PathVariable Integer studentId) {
        return ResponseEntity.ok(studentMarkService.findByStudentId(studentId));
    }

    @PostMapping
    public ResponseEntity<StudentMarkDTO> create(@Valid @RequestBody StudentMarkDTO dto) {
        return ResponseEntity.ok(studentMarkService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentMarkDTO> update(@PathVariable Integer id, @Valid @RequestBody StudentMarkDTO dto) {
        return ResponseEntity.ok(studentMarkService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        studentMarkService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 