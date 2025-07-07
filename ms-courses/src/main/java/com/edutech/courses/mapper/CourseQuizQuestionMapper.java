package com.edutech.courses.mapper;

import com.edutech.common.dto.CourseQuizQuestionDTO;
import com.edutech.courses.entity.CourseQuizQuestion;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseQuizQuestionMapper {
    CourseQuizQuestionDTO toDTO(CourseQuizQuestion entity);
    CourseQuizQuestion toEntity(CourseQuizQuestionDTO dto);
}
