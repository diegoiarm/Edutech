package com.edutech.courses.assembler;

import com.edutech.common.dto.EnrollmentDTO;
import com.edutech.courses.controller.EnrollmentController;
import com.edutech.courses.controller.CourseController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler para enriquecer los DTOs de Enrollment con enlaces HATEOAS.
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
public class EnrollmentAssembler implements RepresentationModelAssembler<EnrollmentDTO, EntityModel<EnrollmentDTO>> {
    
    @Override
    public EntityModel<EnrollmentDTO> toModel(EnrollmentDTO dto) {
        return EntityModel.of(dto,
            // Enlace a sí mismo (self)
            linkTo(methodOn(EnrollmentController.class).findById(dto.getId())).withSelfRel(),
            
            // Enlaces de operaciones CRUD
            linkTo(methodOn(EnrollmentController.class).update(dto.getId(), dto)).withRel("update"),
            linkTo(methodOn(EnrollmentController.class).delete(dto.getId())).withRel("delete"),
            
            // Enlaces a recursos relacionados
            linkTo(methodOn(EnrollmentController.class).findAll()).withRel("enrollments"),
            linkTo(methodOn(CourseController.class).findById(dto.getCourseId())).withRel("course"),
            
            // Enlaces específicos del dominio
            linkTo(methodOn(EnrollmentController.class).findById(dto.getId())).withRel("progress"),
            linkTo(methodOn(EnrollmentController.class).findById(dto.getId())).withRel("certificate")
        );
    }
} 