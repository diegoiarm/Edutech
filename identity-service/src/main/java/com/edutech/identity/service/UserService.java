package com.edutech.identity.service;

import com.edutech.common.dto.UserDTO;
import com.edutech.identity.entity.User;
import com.edutech.identity.mapper.UserMapper;
import com.edutech.identity.repository.UserRepository;
import static com.edutech.common.exception.ExceptionUtils.orThrow;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public UserDTO create(UserDTO dto) {
        return saveDTO(dto, null);
    }

    public UserDTO update(Integer id, UserDTO dto) {
        orThrow(userRepo.findById(id), "Usuario");
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
