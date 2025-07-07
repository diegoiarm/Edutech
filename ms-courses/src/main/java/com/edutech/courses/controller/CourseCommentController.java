package com.edutech.courses.controller;

import com.edutech.common.dto.CourseCommentDTO;
import com.edutech.courses.service.CourseCommentService;
import com.edutech.courses.assembler.CourseCommentAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/course-comments")
@RequiredArgsConstructor
public class CourseCommentController {

    private final CourseCommentService commentService;
    private final CourseCommentAssembler courseCommentAssembler;

    @GetMapping
    public CollectionModel<EntityModel<CourseCommentDTO>> findAll() {
        var comments = commentService.findAll().stream()
            .map(courseCommentAssembler::toModel)
            .toList();
        return CollectionModel.of(comments, linkTo(methodOn(CourseCommentController.class).findAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CourseCommentDTO> findById(@PathVariable Integer id) {
        return courseCommentAssembler.toModel(commentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CourseCommentDTO> create(@RequestBody @Valid CourseCommentDTO dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseCommentDTO> update(@PathVariable Integer id, @RequestBody @Valid CourseCommentDTO dto) {
        return ResponseEntity.ok(commentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

// Endpoints RESTful para comentarios por curso
@RestController
@RequiredArgsConstructor
class CourseCommentByCourseController {
    private final CourseCommentService commentService;
    private final CourseCommentAssembler courseCommentAssembler;

    @GetMapping("/api/courses/{courseId}/comments")
    public CollectionModel<EntityModel<CourseCommentDTO>> findCommentsByCourseId(@PathVariable Integer courseId) {
        var comments = commentService.findCommentsByCourseId(courseId).stream()
            .map(courseCommentAssembler::toModel)
            .toList();
        return CollectionModel.of(comments, linkTo(methodOn(CourseCommentByCourseController.class).findCommentsByCourseId(courseId)).withSelfRel());
    }

    @PostMapping("/api/courses/{courseId}/comments")
    public ResponseEntity<CourseCommentDTO> createForCourse(@PathVariable Integer courseId, @RequestBody @Valid CourseCommentDTO dto) {
        return ResponseEntity.ok(commentService.createForCourse(courseId, dto));
    }
} 