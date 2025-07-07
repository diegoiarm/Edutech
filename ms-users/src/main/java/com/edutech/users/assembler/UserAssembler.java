package com.edutech.users.assembler;

import com.edutech.common.dto.UserDTO;
import com.edutech.users.controller.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {
    @Override
    public EntityModel<UserDTO> toModel(UserDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(UserController.class).findById(dto.getId())).withSelfRel(),
            linkTo(methodOn(UserController.class).findAll()).withRel("users")
        );
    }
} 