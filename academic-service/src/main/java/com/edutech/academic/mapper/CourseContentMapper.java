package com.edutech.academic.mapper;

import com.edutech.common.dto.CourseContentDTO;
import com.edutech.academic.entity.CourseContent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseContentMapper {
    CourseContentDTO toDTO(CourseContent entity);
    CourseContent toEntity(CourseContentDTO dto);
}
