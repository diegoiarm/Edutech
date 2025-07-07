package com.edutech.users.controller;

import com.edutech.common.dto.RoleDTO;
import com.edutech.users.service.*;
import com.edutech.users.assembler.RoleAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleAssembler roleAssembler;

    @GetMapping
    public CollectionModel<EntityModel<RoleDTO>> findAll() {
        var roles = roleService.findAll().stream()
            .map(roleAssembler::toModel)
            .toList();
        return CollectionModel.of(roles, linkTo(methodOn(RoleController.class).findAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<RoleDTO> findById(@PathVariable Integer id) {
        return roleAssembler.toModel(roleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoleDTO> create(@RequestBody @Valid RoleDTO dto) {
        return ResponseEntity.ok(roleService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> update(@PathVariable Integer id, @RequestBody @Valid RoleDTO dto) {
        return ResponseEntity.ok(roleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
