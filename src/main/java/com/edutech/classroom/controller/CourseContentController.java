package com.edutech.classroom.controller;

import com.edutech.classroom.dto.CourseContentDTO;
import com.edutech.classroom.service.CourseContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/content")
@RequiredArgsConstructor
public class CourseContentController {

    private final CourseContentService contentService;

    @GetMapping
    public ResponseEntity<List<CourseContentDTO>> getAllContent() {
        return ResponseEntity.ok(contentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseContentDTO> getContentById(@PathVariable Integer id) {
        return ResponseEntity.ok(contentService.findById(id));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseContentDTO>> getContentByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(contentService.findByCourse(courseId));
    }

    @GetMapping("/course/{courseId}/type/{contentType}")
    public ResponseEntity<List<CourseContentDTO>> getContentByCourseAndType(
            @PathVariable Integer courseId,
            @PathVariable String contentType) {
        return ResponseEntity.ok(contentService.findByCourseAndType(courseId, contentType));
    }

    @PostMapping
    public ResponseEntity<CourseContentDTO> createContent(@Valid @RequestBody CourseContentDTO contentDTO) {
        return ResponseEntity.ok(contentService.create(contentDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseContentDTO> updateContent(
            @PathVariable Integer id,
            @Valid @RequestBody CourseContentDTO contentDTO) {
        return ResponseEntity.ok(contentService.update(id, contentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Integer id) {
        contentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/course/{courseId}/reorder")
    public ResponseEntity<Void> reorderContent(
            @PathVariable Integer courseId,
            @RequestBody List<Integer> contentIds) {
        contentService.reorderContent(courseId, contentIds);
        return ResponseEntity.noContent().build();
    }
} 