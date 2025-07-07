package com.edutech.users.controller;

import com.edutech.common.dto.UserDTO;
import com.edutech.users.service.*;
import com.edutech.users.assembler.UserAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserAssembler userAssembler;

    @GetMapping
    public CollectionModel<EntityModel<UserDTO>> findAll() {
        var users = userService.findAll().stream()
            .map(userAssembler::toModel)
            .toList();
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).findAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<UserDTO> findById(@PathVariable Integer id) {
        return userAssembler.toModel(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Integer id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
