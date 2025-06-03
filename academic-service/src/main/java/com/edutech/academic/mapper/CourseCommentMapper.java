package com.edutech.academic.mapper;

import com.edutech.common.dto.CourseCommentDTO;
import com.edutech.academic.entity.CourseComment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseCommentMapper {
    CourseCommentDTO toDTO(CourseComment entity);
    CourseComment toEntity(CourseCommentDTO dto);
}
