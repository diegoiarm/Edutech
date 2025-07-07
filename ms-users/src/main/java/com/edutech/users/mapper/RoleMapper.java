package com.edutech.users.mapper;

import com.edutech.common.dto.RoleDTO;
import com.edutech.users.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RoleMapper {
    RoleDTO toDTO(Role entity);
    Role toEntity(RoleDTO dto);
}
