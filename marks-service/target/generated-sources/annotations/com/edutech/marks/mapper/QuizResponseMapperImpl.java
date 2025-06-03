package com.edutech.marks.mapper;

import com.edutech.common.dto.QuizResponseDTO;
import com.edutech.marks.entity.QuizResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T01:12:04-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class QuizResponseMapperImpl implements QuizResponseMapper {

    @Override
    public QuizResponseDTO toDTO(QuizResponse entity) {
        if ( entity == null ) {
            return null;
        }

        QuizResponseDTO quizResponseDTO = new QuizResponseDTO();

        return quizResponseDTO;
    }

    @Override
    public QuizResponse toEntity(QuizResponseDTO dto) {
        if ( dto == null ) {
            return null;
        }

        QuizResponse quizResponse = new QuizResponse();

        return quizResponse;
    }
}
