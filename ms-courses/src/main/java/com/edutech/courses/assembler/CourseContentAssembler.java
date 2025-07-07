package com.edutech.courses.assembler;

import com.edutech.common.dto.CourseContentDTO;
import com.edutech.courses.controller.CourseContentController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler para enriquecer los DTOs de CourseContent con enlaces HATEOAS.
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
public class CourseContentAssembler implements RepresentationModelAssembler<CourseContentDTO, EntityModel<CourseContentDTO>> {
    
    @Override
    public EntityModel<CourseContentDTO> toModel(CourseContentDTO dto) {
        return EntityModel.of(dto,
            // Enlace a sí mismo (self)
            linkTo(methodOn(CourseContentController.class).findById(dto.getId())).withSelfRel(),
            
            // Enlaces de operaciones CRUD
            linkTo(methodOn(CourseContentController.class).update(dto.getId(), dto)).withRel("update"),
            linkTo(methodOn(CourseContentController.class).delete(dto.getId())).withRel("delete"),
            
            // Enlaces a recursos relacionados
            linkTo(methodOn(CourseContentController.class).findAll()).withRel("contents"),
            
            // Enlaces específicos del dominio
            linkTo(methodOn(CourseContentController.class).findAll()).withRel("course-contents")
        );
    }
} 