package com.edutech.classroom.controller;

import com.edutech.classroom.dto.CourseCommentDTO;
import com.edutech.classroom.service.CourseCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/comments")
@RequiredArgsConstructor
public class CourseCommentController {

    private final CourseCommentService commentService;

    @GetMapping
    public ResponseEntity<List<CourseCommentDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseCommentDTO> getCommentById(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseCommentDTO>> getCommentsByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(commentService.findByCourse(courseId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CourseCommentDTO>> getCommentsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(commentService.findByUser(userId));
    }

    @GetMapping("/course/{courseId}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Integer courseId) {
        return ResponseEntity.ok(commentService.getAverageRating(courseId));
    }

    @PostMapping
    public ResponseEntity<CourseCommentDTO> createComment(@Valid @RequestBody CourseCommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.create(commentDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseCommentDTO> updateComment(
            @PathVariable Integer id,
            @Valid @RequestBody CourseCommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.update(id, commentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 