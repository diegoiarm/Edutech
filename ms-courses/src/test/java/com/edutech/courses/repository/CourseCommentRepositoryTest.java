package com.edutech.courses.repository;

import com.edutech.courses.entity.CourseComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CourseCommentRepositoryTest {
    @Autowired
    private CourseCommentRepository commentRepository;

    /**
     * Este test verifica que se puede guardar un comentario de curso en la base de datos
     * y luego recuperarlo correctamente usando su ID.
     * Se asegura que el campo 'commentText' se persiste y recupera sin errores.
     */
    @Test
    @DisplayName("Guardar y buscar un comentario por ID")
    void testSaveAndFindById() {
        CourseComment comment = new CourseComment();
        comment.setCommentText("Buen curso");
        comment.setCourseId(1);
        comment.setUserId(1);
        comment.setRating(5);
        comment.setCreatedAt(java.time.Instant.now());
        comment = commentRepository.save(comment);
        Optional<CourseComment> found = commentRepository.findById(comment.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getCommentText()).isEqualTo("Buen curso");
        // Mensaje de éxito en consola
        System.out.println("✅ Test 'Guardar y buscar un comentario por ID' pasó correctamente.");
    }

    /**
     * Este test verifica que se pueden recuperar todos los comentarios guardados en la base de datos.
     */
    @Test
    @DisplayName("Buscar todos los comentarios")
    void testFindAll() {
        CourseComment c1 = new CourseComment();
        c1.setCommentText("Buen curso");
        c1.setCourseId(1);
        c1.setUserId(1);
        c1.setRating(5);
        c1.setCreatedAt(java.time.Instant.now());
        commentRepository.save(c1);
        CourseComment c2 = new CourseComment();
        c2.setCommentText("Excelente");
        c2.setCourseId(1);
        c2.setUserId(2);
        c2.setRating(4);
        c2.setCreatedAt(java.time.Instant.now());
        commentRepository.save(c2);
        List<CourseComment> all = commentRepository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
        System.out.println("✅ Test 'Buscar todos los comentarios' pasó correctamente.");
    }

    /**
     * Este test verifica que se puede actualizar un comentario existente.
     */
    @Test
    @DisplayName("Actualizar un comentario")
    void testUpdate() {
        CourseComment comment = new CourseComment();
        comment.setCommentText("Buen curso");
        comment.setCourseId(1);
        comment.setUserId(1);
        comment.setRating(5);
        comment.setCreatedAt(java.time.Instant.now());
        comment = commentRepository.save(comment);
        comment.setCommentText("Comentario actualizado");
        comment = commentRepository.save(comment);
        Optional<CourseComment> found = commentRepository.findById(comment.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getCommentText()).isEqualTo("Comentario actualizado");
        System.out.println("✅ Test 'Actualizar un comentario' pasó correctamente.");
    }

    /**
     * Este test verifica que se puede eliminar un comentario existente.
     */
    @Test
    @DisplayName("Eliminar un comentario")
    void testDelete() {
        CourseComment comment = new CourseComment();
        comment.setCommentText("Buen curso");
        comment.setCourseId(1);
        comment.setUserId(1);
        comment.setRating(5);
        comment.setCreatedAt(java.time.Instant.now());
        comment = commentRepository.save(comment);
        commentRepository.deleteById(comment.getId());
        Optional<CourseComment> found = commentRepository.findById(comment.getId());
        assertThat(found).isNotPresent();
        System.out.println("✅ Test 'Eliminar un comentario' pasó correctamente.");
    }
} 