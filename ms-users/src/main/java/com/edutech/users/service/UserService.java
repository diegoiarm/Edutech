package com.edutech.users.service;

import com.edutech.common.dto.UserDTO;
import com.edutech.users.entity.User;
import com.edutech.users.mapper.UserMapper;
import com.edutech.users.repository.UserRepository;
import static com.edutech.common.exception.ExceptionUtils.orThrow;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final UserMapper userMapper;


    public List<UserDTO> findAll() {
        return userRepo.findAll().stream().map(userMapper::toDTO).toList();
    }

    public UserDTO findById(Integer id) {
        return userMapper.toDTO(orThrow(userRepo.findById(id), "Usuario"));
    }

    public UserDTO findByEmail(String email) {
        return userMapper.toDTO(orThrow(userRepo.findByEmail(email), "Usuario"));
    }

    public UserDTO create(UserDTO dto) {

        // Verificar que el nuevo usuario no tenga un email repetido, que ya usa otro usuario
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "El email '" + dto.getEmail() + "' ya está registrado.");
        }

        return saveDTO(dto, null);
    }

    public UserDTO update(Integer id, UserDTO dto) {
        // 1. Primero, buscamos el usuario que se va a actualizar para tener su estado actual.
        // Si no existe, orThrow lanzará un 404 Not Found, lo cual es correcto.
        User existingUser = orThrow(userRepo.findById(id), "Usuario");

        // 2. Comparamos el email actual del usuario con el que viene en la petición (DTO).
        // Verificamos si el email ha cambiado.
        boolean emailHasChanged = !existingUser.getEmail().equals(dto.getEmail());

        // 3. Si el email ha cambiado, debemos verificar si el NUEVO email ya está en uso por otro usuario.
        if (emailHasChanged && userRepo.existsByEmail(dto.getEmail())) {
            // Si ambas condiciones son verdaderas, lanzamos el error de conflicto.
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "El email '" + dto.getEmail() + "' ya está en uso por otro usuario.");
        }
        
        // 4. Si todas las validaciones pasan, procedemos a guardar los cambios.
        // El método saveDTO se encargará de mapear y persistir.
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        userRepo.delete(orThrow(userRepo.findById(id), "Usuario"));
    }

    private UserDTO saveDTO(UserDTO dto, Integer id) {
        User entity = userMapper.toEntity(dto);
        if (id != null) entity.setId(id);
        return userMapper.toDTO(userRepo.save(entity));
    }
}
