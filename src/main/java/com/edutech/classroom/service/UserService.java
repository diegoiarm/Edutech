package com.edutech.classroom.service;

import com.edutech.classroom.dto.UserDTO;
import com.edutech.classroom.entity.Role;
import com.edutech.classroom.entity.User;
import com.edutech.classroom.exception.ResourceNotFoundException;
import com.edutech.classroom.exception.ValidationException;
import com.edutech.classroom.repository.RoleRepository;
import com.edutech.classroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .toList();
    }

    public UserDTO findById(Integer id) {
        return UserDTO.fromEntity(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado.")));
    }

    public UserDTO findByEmail(String email) {
        return UserDTO.fromEntity(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado.")));
    }

    @Transactional
    public UserDTO create(UserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException("El email ya está registrado.");
        }

        User user = dto.toEntity();
        Role role = roleRepository.findByName(dto.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado."));
        user.setRole(role);

        return UserDTO.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserDTO update(Integer id, UserDTO dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        // Verificar si el email ya existe en otro usuario
        if (!existingUser.getEmail().equals(dto.getEmail()) && 
            userRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException("El email ya está registrado.");
        }

        User user = dto.toEntity();
        Role role = roleRepository.findByName(dto.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado."));
        
        user.setId(id);
        user.setRole(role);
        user.setCreatedAt(existingUser.getCreatedAt()); // Mantener la fecha de creación original

        return UserDTO.fromEntity(userRepository.save(user));
    }

    @Transactional
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserDTO deactivate(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
        user.setIsActive(false);
        return UserDTO.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserDTO activate(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
        user.setIsActive(true);
        return UserDTO.fromEntity(userRepository.save(user));
    }
} 