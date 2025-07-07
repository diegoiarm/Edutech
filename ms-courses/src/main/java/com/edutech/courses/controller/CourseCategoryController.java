package com.edutech.courses.controller;

import com.edutech.common.dto.CourseCategoryDTO;
import com.edutech.courses.service.CourseCategoryService;
import com.edutech.courses.assembler.CourseCategoryAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/course-categories")
@RequiredArgsConstructor
public class CourseCategoryController {

    private final CourseCategoryService categService;
    private final CourseCategoryAssembler courseCategoryAssembler;

    @GetMapping
    public CollectionModel<EntityModel<CourseCategoryDTO>> findAll() {
        var categories = categService.findAll().stream()
            .map(courseCategoryAssembler::toModel)
            .toList();
        return CollectionModel.of(categories, linkTo(methodOn(CourseCategoryController.class).findAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CourseCategoryDTO> findById(@PathVariable Integer id) {
        return courseCategoryAssembler.toModel(categService.findById(id));
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

    @GetMapping("/../courses/{courseId}/categories")
    public EntityModel<CourseCategoryDTO> findCategoryByCourseId(@PathVariable Integer courseId) {
        return courseCategoryAssembler.toModel(categService.findCategoryByCourseId(courseId));
    }

    @PostMapping("/../courses/{courseId}/categories")
    public ResponseEntity<CourseCategoryDTO> createForCourse(@PathVariable Integer courseId, @RequestBody @Valid CourseCategoryDTO dto) {
        return ResponseEntity.ok(categService.createForCourse(courseId, dto));
    }
}

// NUEVO: Endpoints RESTful para categorías por curso
@RestController
@RequiredArgsConstructor
class CourseCategoryByCourseController {
    private final CourseCategoryService categService;
    private final CourseCategoryAssembler courseCategoryAssembler;

    @GetMapping("/api/courses/{courseId}/categories")
    public EntityModel<CourseCategoryDTO> findCategoryByCourseId(@PathVariable Integer courseId) {
        return courseCategoryAssembler.toModel(categService.findCategoryByCourseId(courseId));
    }

    @PostMapping("/api/courses/{courseId}/categories")
    public ResponseEntity<CourseCategoryDTO> createForCourse(@PathVariable Integer courseId, @RequestBody @Valid CourseCategoryDTO dto) {
        return ResponseEntity.ok(categService.createForCourse(courseId, dto));
    }
}
