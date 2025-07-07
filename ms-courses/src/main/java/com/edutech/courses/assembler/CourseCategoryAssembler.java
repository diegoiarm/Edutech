package com.edutech.courses.assembler;

import com.edutech.common.dto.CourseCategoryDTO;
import com.edutech.courses.controller.CourseCategoryController;
import com.edutech.courses.controller.CourseController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler para enriquecer los DTOs de CourseCategory con enlaces HATEOAS.
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
public class CourseCategoryAssembler implements RepresentationModelAssembler<CourseCategoryDTO, EntityModel<CourseCategoryDTO>> {
    
    @Override
    public EntityModel<CourseCategoryDTO> toModel(CourseCategoryDTO dto) {
        return EntityModel.of(dto,
            // Enlace a sí mismo (self)
            linkTo(methodOn(CourseCategoryController.class).findById(dto.getId())).withSelfRel(),
            
            // Enlaces de operaciones CRUD
            linkTo(methodOn(CourseCategoryController.class).update(dto.getId(), dto)).withRel("update"),
            linkTo(methodOn(CourseCategoryController.class).delete(dto.getId())).withRel("delete"),
            
            // Enlaces a recursos relacionados
            linkTo(methodOn(CourseCategoryController.class).findAll()).withRel("categories"),
            linkTo(methodOn(CourseController.class).findAll()).withRel("courses"),
            
            // Enlaces específicos del dominio
            linkTo(methodOn(CourseController.class).findAll()).withRel("courses-by-category")
        );
    }
} 