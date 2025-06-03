package com.edutech.classroom.controller;

import com.edutech.classroom.dto.CourseCategoryDTO;
import com.edutech.classroom.service.CourseCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-categories")
@RequiredArgsConstructor
public class CourseCategoryController {
    private final CourseCategoryService service;

    @GetMapping
    public ResponseEntity<List<CourseCategoryDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseCategoryDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CourseCategoryDTO> create(@RequestBody @Valid CourseCategoryDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseCategoryDTO> update(@PathVariable Integer id, @Valid @RequestBody CourseCategoryDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
