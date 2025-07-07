package com.edutech.courses.controller;

import com.edutech.common.dto.CourseDTO;
import com.edutech.courses.service.CourseService;
import com.edutech.courses.assembler.CourseAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseAssembler courseAssembler;

    @GetMapping
    public CollectionModel<EntityModel<CourseDTO>> findAll() {
        var courses = courseService.findAll().stream()
            .map(courseAssembler::toModel)
            .toList();
        return CollectionModel.of(courses, linkTo(methodOn(CourseController.class).findAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CourseDTO> findById(@PathVariable Integer id) {
        return courseAssembler.toModel(courseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CourseDTO> create(@RequestBody @Valid CourseDTO dto) {
        return ResponseEntity.ok(courseService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable Integer id, @RequestBody @Valid CourseDTO dto) {
        return ResponseEntity.ok(courseService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
