package com.edutech.courses.service;

import com.edutech.common.dto.CourseCommentDTO;
import com.edutech.courses.entity.CourseComment;
import com.edutech.courses.mapper.CourseCommentMapper;
import com.edutech.courses.repository.CourseCommentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.edutech.common.exception.ExceptionUtils.orThrow;

@Service
@RequiredArgsConstructor
public class CourseCommentService {
    private final CourseCommentRepository commentRepository;
    private final CourseCommentMapper commentMapper;

    public List<CourseCommentDTO> findAll() {
        return commentRepository.findAll().stream().map(commentMapper::toDTO).toList();
    }

    public CourseCommentDTO findById(Integer id) {
        return commentMapper.toDTO(orThrow(commentRepository.findById(id), "Comentario"));
    }

    public CourseCommentDTO create(CourseCommentDTO dto) {
        return saveDTO(dto, null);
    }

    public CourseCommentDTO update(Integer id, CourseCommentDTO dto) {
        orThrow(commentRepository.findById(id), "Comentario");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        commentRepository.delete(orThrow(commentRepository.findById(id), "Comentario"));
    }

    private CourseCommentDTO saveDTO(CourseCommentDTO dto, Integer id) {
        CourseComment entity = commentMapper.toEntity(dto);
        if (id != null) entity.setId(id);
        if (entity.getCreatedAt() == null) entity.setCreatedAt(Instant.now());
        return commentMapper.toDTO(commentRepository.save(entity));
    }

    // NUEVO: Obtener comentarios de un curso específico
    public List<CourseCommentDTO> findCommentsByCourseId(Integer courseId) {
        return commentRepository.findByCourseId(courseId).stream()
            .map(commentMapper::toDTO)
            .toList();
    }

    // NUEVO: Crear un comentario y asociarlo a un curso
    public CourseCommentDTO createForCourse(Integer courseId, CourseCommentDTO dto) {
        dto.setCourseId(courseId);
        if (dto.getCreatedAt() == null) dto.setCreatedAt(Instant.now());
        return saveDTO(dto, null);
    }

    public CourseComment save(CourseComment comment) {
        return commentRepository.save(comment);
    }

    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }
} 