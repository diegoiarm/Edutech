package com.edutech.identity.mapper;

import com.edutech.common.dto.RoleDTO;
import com.edutech.identity.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toDTO(Role entity);
    Role toEntity(RoleDTO dto);
}
