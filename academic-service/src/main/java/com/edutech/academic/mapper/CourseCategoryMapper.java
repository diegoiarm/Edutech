package com.edutech.academic.mapper;

import com.edutech.common.dto.CourseCategoryDTO;
import com.edutech.academic.entity.CourseCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseCategoryMapper {
    CourseCategoryDTO toDTO(CourseCategory entity);
    CourseCategory toEntity(CourseCategoryDTO dto);
}
