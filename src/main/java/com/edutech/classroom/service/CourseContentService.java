package com.edutech.classroom.service;

import com.edutech.classroom.dto.CourseContentDTO;
import com.edutech.classroom.entity.Course;
import com.edutech.classroom.entity.CourseContent;
import com.edutech.classroom.exception.ResourceNotFoundException;
import com.edutech.classroom.exception.ValidationException;
import com.edutech.classroom.repository.CourseContentRepository;
import com.edutech.classroom.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseContentService {

    private final CourseContentRepository contentRepository;
    private final CourseRepository courseRepository;

    public List<CourseContentDTO> findAll() {
        return contentRepository.findAll().stream()
                .map(CourseContentDTO::fromEntity)
                .toList();
    }

    public CourseContentDTO findById(Integer id) {
        return CourseContentDTO.fromEntity(contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contenido no encontrado.")));
    }

    public List<CourseContentDTO> findByCourse(Integer courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Curso no encontrado.");
        }
        return contentRepository.findByCourseIdOrderByOrderIndexAsc(courseId).stream()
                .map(CourseContentDTO::fromEntity)
                .toList();
    }

    public List<CourseContentDTO> findByCourseAndType(Integer courseId, String contentType) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Curso no encontrado.");
        }
        return contentRepository.findByCourseIdAndContentType(courseId, contentType).stream()
                .map(CourseContentDTO::fromEntity)
                .toList();
    }

    @Transactional
    public CourseContentDTO create(CourseContentDTO dto) {
        // Validar que el curso existe
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));

        // Validar que no existe un contenido con el mismo título en el curso
        if (contentRepository.existsByCourseIdAndTitle(dto.getCourseId(), dto.getTitle())) {
            throw new ValidationException("Ya existe un contenido con ese título en este curso.");
        }

        CourseContent content = dto.toEntity();
        content.setCourse(course);

        return CourseContentDTO.fromEntity(contentRepository.save(content));
    }

    @Transactional
    public CourseContentDTO update(Integer id, CourseContentDTO dto) {
        CourseContent existingContent = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contenido no encontrado."));

        // Validar que el curso existe
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));

        // Validar que no existe otro contenido con el mismo título en el curso
        if (!existingContent.getTitle().equals(dto.getTitle()) &&
            contentRepository.existsByCourseIdAndTitle(dto.getCourseId(), dto.getTitle())) {
            throw new ValidationException("Ya existe un contenido con ese título en este curso.");
        }

        CourseContent content = dto.toEntity();
        content.setId(id);
        content.setCourse(course);

        return CourseContentDTO.fromEntity(contentRepository.save(content));
    }

    @Transactional
    public void delete(Integer id) {
        if (!contentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contenido no encontrado.");
        }
        contentRepository.deleteById(id);
    }

    @Transactional
    public void reorderContent(Integer courseId, List<Integer> contentIds) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Curso no encontrado.");
        }

        // Validar que todos los contenidos pertenecen al curso
        List<CourseContent> contents = contentRepository.findByCourseId(courseId);
        if (contents.size() != contentIds.size()) {
            throw new ValidationException("La lista de IDs no coincide con los contenidos del curso.");
        }

        // Actualizar el orden de cada contenido
        for (int i = 0; i < contentIds.size(); i++) {
            final int index = i;
            CourseContent content = contents.stream()
                    .filter(c -> c.getId().equals(contentIds.get(index)))
                    .findFirst()
                    .orElseThrow(() -> new ValidationException("Contenido no encontrado en el curso."));
            content.setOrderIndex(i + 1);
            contentRepository.save(content);
        }
    }
} 