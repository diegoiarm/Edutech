package com.edutech.classroom.service;

import com.edutech.classroom.dto.CourseCommentDTO;
import com.edutech.classroom.entity.Course;
import com.edutech.classroom.entity.CourseComment;
import com.edutech.classroom.entity.User;
import com.edutech.classroom.exception.ResourceNotFoundException;
import com.edutech.classroom.exception.ValidationException;
import com.edutech.classroom.repository.CourseCommentRepository;
import com.edutech.classroom.repository.CourseRepository;
import com.edutech.classroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseCommentService {

    private final CourseCommentRepository commentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public List<CourseCommentDTO> findAll() {
        return commentRepository.findAll().stream()
                .map(CourseCommentDTO::fromEntity)
                .toList();
    }

    public CourseCommentDTO findById(Integer id) {
        return CourseCommentDTO.fromEntity(commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado.")));
    }

    public List<CourseCommentDTO> findByCourse(Integer courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Curso no encontrado.");
        }
        return commentRepository.findByCourseIdOrderByCreatedAtDesc(courseId).stream()
                .map(CourseCommentDTO::fromEntity)
                .toList();
    }

    public List<CourseCommentDTO> findByUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }
        return commentRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(CourseCommentDTO::fromEntity)
                .toList();
    }

    public double getAverageRating(Integer courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Curso no encontrado.");
        }
        return commentRepository.findAverageRatingByCourseId(courseId);
    }

    @Transactional
    public CourseCommentDTO create(CourseCommentDTO dto) {
        // Validar que el curso existe
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));

        // Validar que el usuario existe y está activo
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
        if (!user.getIsActive()) {
            throw new ValidationException("El usuario no está activo.");
        }

        // Validar que el usuario no ha comentado antes en este curso
        if (commentRepository.existsByCourseIdAndUserId(dto.getCourseId(), dto.getUserId())) {
            throw new ValidationException("El usuario ya ha comentado en este curso.");
        }

        CourseComment comment = dto.toEntity();
        comment.setCourse(course);
        comment.setUser(user);
        comment.setCreatedAt(Instant.now());

        return CourseCommentDTO.fromEntity(commentRepository.save(comment));
    }

    @Transactional
    public CourseCommentDTO update(Integer id, CourseCommentDTO dto) {
        CourseComment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado."));

        // Validar que el usuario es el autor del comentario
        if (!existingComment.getUser().getId().equals(dto.getUserId())) {
            throw new ValidationException("No tiene permiso para modificar este comentario.");
        }

        CourseComment comment = dto.toEntity();
        comment.setId(id);
        comment.setCourse(existingComment.getCourse());
        comment.setUser(existingComment.getUser());
        comment.setCreatedAt(existingComment.getCreatedAt());

        return CourseCommentDTO.fromEntity(commentRepository.save(comment));
    }

    @Transactional
    public void delete(Integer id) {
        CourseComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado."));
        commentRepository.delete(comment);
    }
} 