package com.edutech.academic.controller;

import com.edutech.common.dto.CourseCategoryDTO;
import com.edutech.academic.service.CourseCategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-categories")
@RequiredArgsConstructor
public class CourseCategoryController {

    private final CourseCategoryService categService;

    @GetMapping
    public ResponseEntity<List<CourseCategoryDTO>> getAll() {
        return ResponseEntity.ok(categService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseCategoryDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(categService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CourseCategoryDTO> create(@RequestBody @Valid CourseCategoryDTO dto) {
        return ResponseEntity.ok(categService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseCategoryDTO> update(@PathVariable Integer id, @RequestBody @Valid CourseCategoryDTO dto) {
        return ResponseEntity.ok(categService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
