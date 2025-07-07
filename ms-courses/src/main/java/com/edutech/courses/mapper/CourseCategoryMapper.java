package com.edutech.courses.mapper;

import org.mapstruct.Mapper;

import com.edutech.common.dto.CourseCategoryDTO;
import com.edutech.courses.entity.CourseCategory;

@Mapper(componentModel = "spring")
public interface CourseCategoryMapper {
    CourseCategoryDTO toDTO(CourseCategory entity);
    CourseCategory toEntity(CourseCategoryDTO dto);
}
