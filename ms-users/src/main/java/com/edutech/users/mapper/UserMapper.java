package com.edutech.users.mapper;

import com.edutech.common.dto.UserDTO;
import com.edutech.users.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User entity);
    User toEntity(UserDTO dto);
}
