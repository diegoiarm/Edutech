package com.edutech.users.service;

import com.edutech.common.dto.RoleDTO;
import com.edutech.users.entity.Role;
import com.edutech.users.mapper.RoleMapper;
import com.edutech.users.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.edutech.common.exception.ExceptionUtils.orThrow;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepo;
    private final RoleMapper roleMapper;

    public List<RoleDTO> findAll() {
        return roleRepo.findAll().stream().map(roleMapper::toDTO).toList();
    }

    public RoleDTO findById(Integer id) {
        return roleMapper.toDTO(orThrow(roleRepo.findById(id), "Rol"));
    }

    public RoleDTO create(RoleDTO dto) {
        return saveDTO(dto, null);
    }

    public RoleDTO update(Integer id, RoleDTO dto) {
        orThrow(roleRepo.findById(id), "Rol");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        roleRepo.delete(orThrow(roleRepo.findById(id), "Rol"));
    }

    private RoleDTO saveDTO(RoleDTO dto, Integer id) {
        Role entity = roleMapper.toEntity(dto);
        if (id != null) entity.setId(id);
        return roleMapper.toDTO(roleRepo.save(entity));
    }
}
