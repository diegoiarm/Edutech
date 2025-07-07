package com.edutech.courses.service;

import com.edutech.common.dto.CourseContentDTO;
import com.edutech.courses.entity.CourseContent;
import com.edutech.courses.mapper.CourseContentMapper;
import com.edutech.courses.repository.CourseContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.edutech.common.exception.ExceptionUtils.orThrow;

@Service
@RequiredArgsConstructor
public class CourseContentService {
    private final CourseContentRepository contentRepository;
    private final CourseContentMapper contentMapper;

    public List<CourseContentDTO> findAll() {
        return contentRepository.findAll().stream().map(contentMapper::toDTO).toList();
    }

    public CourseContentDTO findById(Integer id) {
        return contentMapper.toDTO(orThrow(contentRepository.findById(id), "Contenido"));
    }

    public CourseContentDTO create(CourseContentDTO dto) {
        return saveDTO(dto, null);
    }

    public CourseContentDTO update(Integer id, CourseContentDTO dto) {
        orThrow(contentRepository.findById(id), "Contenido");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        contentRepository.delete(orThrow(contentRepository.findById(id), "Contenido"));
    }

    private CourseContentDTO saveDTO(CourseContentDTO dto, Integer id) {
        CourseContent entity = contentMapper.toEntity(dto);
        if (id != null) entity.setId(id);
        return contentMapper.toDTO(contentRepository.save(entity));
    }

    // NUEVO: Obtener contenidos de un curso específico
    public List<CourseContentDTO> findContentsByCourseId(Integer courseId) {
        return contentRepository.findByCourseId(courseId).stream()
            .map(contentMapper::toDTO)
            .toList();
    }

    // NUEVO: Crear un contenido y asociarlo a un curso
    public CourseContentDTO createForCourse(Integer courseId, CourseContentDTO dto) {
        dto.setCourseId(courseId);
        return saveDTO(dto, null);
    }

    public CourseContent save(CourseContent content) {
        return contentRepository.save(content);
    }

    public void deleteById(Integer id) {
        contentRepository.deleteById(id);
    }
} 