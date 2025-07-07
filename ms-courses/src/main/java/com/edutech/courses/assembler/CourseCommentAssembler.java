package com.edutech.courses.assembler;

import com.edutech.common.dto.CourseCommentDTO;
import com.edutech.courses.controller.CourseCommentController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler para enriquecer los DTOs de CourseComment con enlaces HATEOAS.
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
public class CourseCommentAssembler implements RepresentationModelAssembler<CourseCommentDTO, EntityModel<CourseCommentDTO>> {
    
    @Override
    public EntityModel<CourseCommentDTO> toModel(CourseCommentDTO dto) {
        return EntityModel.of(dto,
            // Enlace a sí mismo (self)
            linkTo(methodOn(CourseCommentController.class).findById(dto.getId())).withSelfRel(),
            
            // Enlaces de operaciones CRUD
            linkTo(methodOn(CourseCommentController.class).update(dto.getId(), dto)).withRel("update"),
            linkTo(methodOn(CourseCommentController.class).delete(dto.getId())).withRel("delete"),
            
            // Enlaces a recursos relacionados
            linkTo(methodOn(CourseCommentController.class).findAll()).withRel("comments"),
            
            // Enlaces específicos del dominio
            linkTo(methodOn(CourseCommentController.class).findAll()).withRel("course-comments")
        );
    }
} 