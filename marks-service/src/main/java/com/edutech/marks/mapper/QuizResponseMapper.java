package com.edutech.marks.mapper;

import com.edutech.common.dto.QuizResponseDTO;
import com.edutech.marks.entity.QuizResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizResponseMapper {
    QuizResponseDTO toDTO(QuizResponse entity);
    QuizResponse toEntity(QuizResponseDTO dto);
} 