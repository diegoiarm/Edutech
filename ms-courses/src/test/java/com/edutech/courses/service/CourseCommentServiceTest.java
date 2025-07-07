package com.edutech.courses.service;

import com.edutech.courses.entity.CourseComment;
import com.edutech.courses.repository.CourseCommentRepository;
import com.edutech.courses.mapper.CourseCommentMapper;
import com.edutech.common.dto.CourseCommentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseCommentServiceTest {
    @Mock
    private CourseCommentRepository commentRepository;
    @Mock
    private CourseCommentMapper commentMapper;

    @InjectMocks
    private CourseCommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debería retornar todos los comentarios")
    void testFindAll() {
        CourseComment comment1 = new CourseComment();
        comment1.setId(1);
        comment1.setCourseId(10);
        comment1.setUserId(5);
        comment1.setCommentText("Excelente curso");
        comment1.setRating(5);
        comment1.setCreatedAt(Instant.now());

        CourseComment comment2 = new CourseComment();
        comment2.setId(2);
        comment2.setCourseId(10);
        comment2.setUserId(6);
        comment2.setCommentText("Muy bueno");
        comment2.setRating(4);
        comment2.setCreatedAt(Instant.now());

        CourseCommentDTO dto1 = new CourseCommentDTO();
        dto1.setId(1);
        dto1.setCourseId(10);
        dto1.setUserId(5);
        dto1.setCommentText("Excelente curso");
        dto1.setRating(5);
        dto1.setCreatedAt(comment1.getCreatedAt());

        CourseCommentDTO dto2 = new CourseCommentDTO();
        dto2.setId(2);
        dto2.setCourseId(10);
        dto2.setUserId(6);
        dto2.setCommentText("Muy bueno");
        dto2.setRating(4);
        dto2.setCreatedAt(comment2.getCreatedAt());

        when(commentRepository.findAll()).thenReturn(Arrays.asList(comment1, comment2));
        when(commentMapper.toDTO(comment1)).thenReturn(dto1);
        when(commentMapper.toDTO(comment2)).thenReturn(dto2);

        List<CourseCommentDTO> result = commentService.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCommentText()).isEqualTo("Excelente curso");
        assertThat(result.get(1).getCommentText()).isEqualTo("Muy bueno");
        verify(commentRepository).findAll();
        System.out.println("✅ testFindAll en CourseCommentService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería retornar un comentario por ID")
    void testFindById() {
        CourseComment comment = new CourseComment();
        comment.setId(1);
        comment.setCourseId(10);
        comment.setUserId(5);
        comment.setCommentText("Excelente curso");
        comment.setRating(5);
        comment.setCreatedAt(Instant.now());

        CourseCommentDTO dto = new CourseCommentDTO();
        dto.setId(1);
        dto.setCourseId(10);
        dto.setUserId(5);
        dto.setCommentText("Excelente curso");
        dto.setRating(5);
        dto.setCreatedAt(comment.getCreatedAt());

        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(commentMapper.toDTO(comment)).thenReturn(dto);

        CourseCommentDTO result = commentService.findById(1);
        assertThat(result).isNotNull();
        assertThat(result.getCommentText()).isEqualTo("Excelente curso");
        verify(commentRepository).findById(1);
        System.out.println("✅ testFindById en CourseCommentService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería guardar un comentario")
    void testSave() {
        CourseComment comment = new CourseComment();
        comment.setCourseId(10);
        comment.setUserId(5);
        comment.setCommentText("Excelente curso");
        comment.setRating(5);
        comment.setCreatedAt(Instant.now());

        when(commentRepository.save(comment)).thenReturn(comment);
        CourseComment result = commentService.save(comment);
        assertThat(result.getCommentText()).isEqualTo("Excelente curso");
        verify(commentRepository).save(comment);
        System.out.println("✅ testSave en CourseCommentService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería eliminar un comentario por ID")
    void testDeleteById() {
        commentService.deleteById(1);
        verify(commentRepository).deleteById(1);
        System.out.println("✅ testDeleteById en CourseCommentService pasó correctamente.");
    }

    @Test
    @DisplayName("findCommentsByCourseId retorna los comentarios asociados a un curso")
    void testFindCommentsByCourseId() {
        CourseComment comment1 = new CourseComment();
        comment1.setId(1);
        comment1.setCourseId(10);
        comment1.setUserId(5);
        comment1.setCommentText("Excelente curso");
        comment1.setRating(5);
        comment1.setCreatedAt(Instant.now());

        CourseComment comment2 = new CourseComment();
        comment2.setId(2);
        comment2.setCourseId(10);
        comment2.setUserId(6);
        comment2.setCommentText("Muy bueno");
        comment2.setRating(4);
        comment2.setCreatedAt(Instant.now());

        CourseCommentDTO dto1 = new CourseCommentDTO();
        dto1.setId(1);
        dto1.setCourseId(10);
        dto1.setUserId(5);
        dto1.setCommentText("Excelente curso");
        dto1.setRating(5);
        dto1.setCreatedAt(comment1.getCreatedAt());

        CourseCommentDTO dto2 = new CourseCommentDTO();
        dto2.setId(2);
        dto2.setCourseId(10);
        dto2.setUserId(6);
        dto2.setCommentText("Muy bueno");
        dto2.setRating(4);
        dto2.setCreatedAt(comment2.getCreatedAt());

        when(commentRepository.findByCourseId(10)).thenReturn(Arrays.asList(comment1, comment2));
        when(commentMapper.toDTO(comment1)).thenReturn(dto1);
        when(commentMapper.toDTO(comment2)).thenReturn(dto2);

        List<CourseCommentDTO> result = commentService.findCommentsByCourseId(10);
        assertEquals(2, result.size());
        assertEquals("Excelente curso", result.get(0).getCommentText());
        assertEquals("Muy bueno", result.get(1).getCommentText());
    }

    @Test
    @DisplayName("createForCourse crea el comentario y lo asocia al curso")
    void testCreateForCourse() {
        CourseCommentDTO inputDto = new CourseCommentDTO();
        inputDto.setUserId(7);
        inputDto.setCommentText("Nuevo comentario");
        inputDto.setRating(3);

        CourseComment comment = new CourseComment();
        comment.setId(null);
        comment.setCourseId(10);
        comment.setUserId(7);
        comment.setCommentText("Nuevo comentario");
        comment.setRating(3);
        comment.setCreatedAt(Instant.now());

        CourseComment savedComment = new CourseComment();
        savedComment.setId(3);
        savedComment.setCourseId(10);
        savedComment.setUserId(7);
        savedComment.setCommentText("Nuevo comentario");
        savedComment.setRating(3);
        savedComment.setCreatedAt(comment.getCreatedAt());

        CourseCommentDTO resultDto = new CourseCommentDTO();
        resultDto.setId(3);
        resultDto.setCourseId(10);
        resultDto.setUserId(7);
        resultDto.setCommentText("Nuevo comentario");
        resultDto.setRating(3);
        resultDto.setCreatedAt(comment.getCreatedAt());

        when(commentMapper.toEntity(any(CourseCommentDTO.class))).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(savedComment);
        when(commentMapper.toDTO(savedComment)).thenReturn(resultDto);

        CourseCommentDTO result = commentService.createForCourse(10, inputDto);
        assertEquals(3, result.getId());
        assertEquals("Nuevo comentario", result.getCommentText());
        assertEquals(10, result.getCourseId());
    }
} 