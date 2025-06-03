package com.edutech.academic.mapper;

import com.edutech.common.dto.CourseDTO;
import com.edutech.academic.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDTO toDTO(Course entity);
    Course toEntity(CourseDTO dto);
}
