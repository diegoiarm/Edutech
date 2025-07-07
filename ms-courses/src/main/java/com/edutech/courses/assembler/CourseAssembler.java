package com.edutech.courses.assembler;

import com.edutech.common.dto.CourseDTO;
import com.edutech.courses.controller.CourseController;
import com.edutech.courses.controller.EnrollmentController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler para enriquecer los DTOs de Course con enlaces HATEOAS.
 * 
 * Este componente implementa HATEOAS (Hypermedia As The Engine Of Application State)
 * para hacer la API descubrible y navegable por sí sola, sin necesidad de documentación externa.
 * 
 * Los enlaces incluidos permiten al cliente:
 * - Navegar a recursos relacionados
 * - Realizar operaciones CRUD
 * - Descubrir funcionalidades disponibles
 */
@Component
public class CourseAssembler implements RepresentationModelAssembler<CourseDTO, EntityModel<CourseDTO>> {
    
    @Override
    public EntityModel<CourseDTO> toModel(CourseDTO dto) {
        return EntityModel.of(dto,
            // Enlace a sí mismo (self)
            linkTo(methodOn(CourseController.class).findById(dto.getId())).withSelfRel(),
            
            // Enlaces de operaciones CRUD
            linkTo(methodOn(CourseController.class).update(dto.getId(), dto)).withRel("update"),
            linkTo(methodOn(CourseController.class).delete(dto.getId())).withRel("delete"),
            
            // Enlaces a recursos relacionados
            linkTo(methodOn(CourseController.class).findAll()).withRel("courses"),
            linkTo(methodOn(EnrollmentController.class).findAll()).withRel("enrollments"),
            
            // Enlaces específicos del dominio
            linkTo(methodOn(EnrollmentController.class).create(null)).withRel("enroll"),
            linkTo(methodOn(CourseController.class).findById(dto.getId())).withRel("content"),
            linkTo(methodOn(CourseController.class).findById(dto.getId())).withRel("comments")
        );
    }
} 

