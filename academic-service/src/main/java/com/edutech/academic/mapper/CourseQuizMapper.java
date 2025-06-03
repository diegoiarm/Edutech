package com.edutech.academic.mapper;

import com.edutech.common.dto.CourseQuizDTO;
import com.edutech.academic.entity.CourseQuiz;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseQuizMapper {
    CourseQuizDTO toDTO(CourseQuiz entity);
    CourseQuiz toEntity(CourseQuizDTO dto);
}
