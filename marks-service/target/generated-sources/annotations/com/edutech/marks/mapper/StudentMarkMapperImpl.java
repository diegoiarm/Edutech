package com.edutech.marks.mapper;

import com.edutech.common.dto.StudentMarkDTO;
import com.edutech.marks.entity.StudentMark;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T01:12:04-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class StudentMarkMapperImpl implements StudentMarkMapper {

    @Override
    public StudentMarkDTO toDTO(StudentMark entity) {
        if ( entity == null ) {
            return null;
        }

        StudentMarkDTO studentMarkDTO = new StudentMarkDTO();

        return studentMarkDTO;
    }

    @Override
    public StudentMark toEntity(StudentMarkDTO dto) {
        if ( dto == null ) {
            return null;
        }

        StudentMark studentMark = new StudentMark();

        return studentMark;
    }
}
