package com.edutech.courses.mapper;

import com.edutech.courses.entity.Enrollment;
import com.edutech.common.dto.EnrollmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    EnrollmentMapper INSTANCE = Mappers.getMapper(EnrollmentMapper.class);

    EnrollmentDTO toDTO(Enrollment entity);
    Enrollment toEntity(EnrollmentDTO dto);
} 