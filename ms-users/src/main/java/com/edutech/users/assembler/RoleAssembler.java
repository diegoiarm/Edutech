package com.edutech.users.assembler;

import com.edutech.common.dto.RoleDTO;
import com.edutech.users.controller.RoleController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RoleAssembler implements RepresentationModelAssembler<RoleDTO, EntityModel<RoleDTO>> {
    @Override
    public EntityModel<RoleDTO> toModel(RoleDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(RoleController.class).findById(dto.getId())).withSelfRel(),
            linkTo(methodOn(RoleController.class).findAll()).withRel("roles")
        );
    }
} 