package com.edutech.courses.controller;

import com.edutech.courses.entity.Enrollment;
import com.edutech.courses.mapper.EnrollmentMapper;
import com.edutech.courses.service.EnrollmentService;
import com.edutech.common.dto.EnrollmentDTO;
import com.edutech.courses.assembler.EnrollmentAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final EnrollmentMapper enrollmentMapper;
    private final EnrollmentAssembler enrollmentAssembler;

    @GetMapping
    public CollectionModel<EntityModel<EnrollmentDTO>> findAll() {
        var enrollments = enrollmentService.findAll().stream()
            .map(enrollmentMapper::toDTO)
            .map(enrollmentAssembler::toModel)
            .toList();
        return CollectionModel.of(enrollments, linkTo(methodOn(EnrollmentController.class).findAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<EnrollmentDTO> findById(@PathVariable Integer id) {
        var enrollment = enrollmentService.findById(id)
            .map(enrollmentMapper::toDTO)
            .orElseThrow();
        return enrollmentAssembler.toModel(enrollment);
    }

    @PostMapping
    public ResponseEntity<EnrollmentDTO> create(@RequestBody EnrollmentDTO dto) {
        Enrollment entity = enrollmentMapper.toEntity(dto);
        Enrollment saved = enrollmentService.save(entity);
        EnrollmentDTO savedDto = enrollmentMapper.toDTO(saved);
        return ResponseEntity.created(URI.create("/api/enrollments/" + savedDto.getId())).body(savedDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> update(@PathVariable Integer id, @RequestBody EnrollmentDTO dto) {
        if (!enrollmentService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Enrollment entity = enrollmentMapper.toEntity(dto);
        entity.setId(id);
        Enrollment updated = enrollmentService.save(entity);
        return ResponseEntity.ok(enrollmentMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!enrollmentService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        enrollmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

// Endpoints RESTful para inscripciones por curso
@RestController
@RequiredArgsConstructor
class EnrollmentByCourseController {
    private final EnrollmentService enrollmentService;
    private final EnrollmentAssembler enrollmentAssembler;

    @GetMapping("/api/courses/{courseId}/enrollments")
    public CollectionModel<EntityModel<EnrollmentDTO>> findByCourseId(@PathVariable Integer courseId) {
        var enrollments = enrollmentService.findByCourseId(courseId).stream()
            .map(enrollmentAssembler::toModel)
            .toList();
        return CollectionModel.of(enrollments, linkTo(methodOn(EnrollmentByCourseController.class).findByCourseId(courseId)).withSelfRel());
    }

    @PostMapping("/api/courses/{courseId}/enrollments")
    public ResponseEntity<EnrollmentDTO> createForCourse(@PathVariable Integer courseId, @RequestBody @Valid EnrollmentDTO dto) {
        return ResponseEntity.ok(enrollmentService.createForCourse(courseId, dto));
    }
} 