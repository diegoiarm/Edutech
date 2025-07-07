package com.edutech.courses.mapper;

import com.edutech.common.dto.CourseDTO;
import com.edutech.courses.entity.Course;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDTO toDTO(Course entity);
    Course toEntity(CourseDTO dto);
}
