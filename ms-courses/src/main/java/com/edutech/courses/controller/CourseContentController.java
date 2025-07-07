package com.edutech.courses.controller;

import com.edutech.common.dto.CourseContentDTO;
import com.edutech.courses.service.CourseContentService;
import com.edutech.courses.assembler.CourseContentAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/course-contents")
@RequiredArgsConstructor
public class CourseContentController {

    private final CourseContentService contentService;
    private final CourseContentAssembler courseContentAssembler;

    @GetMapping
    public CollectionModel<EntityModel<CourseContentDTO>> findAll() {
        var contents = contentService.findAll().stream()
            .map(courseContentAssembler::toModel)
            .toList();
        return CollectionModel.of(contents, linkTo(methodOn(CourseContentController.class).findAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CourseContentDTO> findById(@PathVariable Integer id) {
        return courseContentAssembler.toModel(contentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CourseContentDTO> create(@RequestBody @Valid CourseContentDTO dto) {
        return ResponseEntity.ok(contentService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseContentDTO> update(@PathVariable Integer id, @RequestBody @Valid CourseContentDTO dto) {
        return ResponseEntity.ok(contentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        contentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

// Endpoints RESTful para contenidos por curso
@RestController
@RequiredArgsConstructor
class CourseContentByCourseController {
    private final CourseContentService contentService;
    private final CourseContentAssembler courseContentAssembler;

    @GetMapping("/api/courses/{courseId}/contents")
    public CollectionModel<EntityModel<CourseContentDTO>> findContentsByCourseId(@PathVariable Integer courseId) {
        var contents = contentService.findContentsByCourseId(courseId).stream()
            .map(courseContentAssembler::toModel)
            .toList();
        return CollectionModel.of(contents, linkTo(methodOn(CourseContentByCourseController.class).findContentsByCourseId(courseId)).withSelfRel());
    }

    @PostMapping("/api/courses/{courseId}/contents")
    public ResponseEntity<CourseContentDTO> createForCourse(@PathVariable Integer courseId, @RequestBody @Valid CourseContentDTO dto) {
        return ResponseEntity.ok(contentService.createForCourse(courseId, dto));
    }
} 