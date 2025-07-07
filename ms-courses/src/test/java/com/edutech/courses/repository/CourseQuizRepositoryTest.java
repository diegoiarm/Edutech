package com.edutech.courses.repository;

import com.edutech.courses.entity.CourseQuiz;
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
class CourseQuizRepositoryTest {
    @Autowired
    private CourseQuizRepository quizRepository;

    /**
     * Este test verifica que se puede guardar un quiz de curso en la base de datos
     * y luego recuperarlo correctamente usando su ID.
     * Se asegura que los campos obligatorios se persisten y recuperan sin errores.
     */
    @Test
    @DisplayName("Guardar y buscar un quiz por ID")
    void testSaveAndFindById() {
        CourseQuiz quiz = new CourseQuiz();
        quiz.setTitle("Quiz 1");
        quiz.setDescription("Primer quiz del curso");
        quiz.setQuizType("AUTOEVALUACION");
        quiz.setCreatedAt(java.time.Instant.now());
        quiz.setCourseId(1);
        quiz = quizRepository.save(quiz);
        Optional<CourseQuiz> found = quizRepository.findById(quiz.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Quiz 1");
        assertThat(found.get().getDescription()).isEqualTo("Primer quiz del curso");
        assertThat(found.get().getQuizType()).isEqualTo("AUTOEVALUACION");
        assertThat(found.get().getCreatedAt()).isNotNull();
        assertThat(found.get().getCourseId()).isEqualTo(1);
        // Mensaje de éxito en consola
        System.out.println("✅ Test 'Guardar y buscar un quiz por ID' pasó correctamente.");
    }

    /**
     * Este test verifica que se pueden recuperar todos los quizzes guardados en la base de datos.
     */
    @Test
    @DisplayName("Buscar todos los quizzes")
    void testFindAll() {
        CourseQuiz q1 = new CourseQuiz();
        q1.setTitle("Quiz 1");
        q1.setDescription("Primer quiz");
        q1.setQuizType("AUTOEVALUACION");
        q1.setCreatedAt(java.time.Instant.now());
        q1.setCourseId(1);
        quizRepository.save(q1);
        CourseQuiz q2 = new CourseQuiz();
        q2.setTitle("Quiz 2");
        q2.setDescription("Segundo quiz");
        q2.setQuizType("FINAL");
        q2.setCreatedAt(java.time.Instant.now());
        q2.setCourseId(1);
        quizRepository.save(q2);
        List<CourseQuiz> all = quizRepository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
        System.out.println("✅ Test 'Buscar todos los quizzes' pasó correctamente.");
    }

    /**
     * Este test verifica que se puede actualizar un quiz existente.
     */
    @Test
    @DisplayName("Actualizar un quiz")
    void testUpdate() {
        CourseQuiz quiz = new CourseQuiz();
        quiz.setTitle("Quiz 1");
        quiz.setDescription("Primer quiz");
        quiz.setQuizType("AUTOEVALUACION");
        quiz.setCreatedAt(java.time.Instant.now());
        quiz.setCourseId(1);
        quiz = quizRepository.save(quiz);
        quiz.setDescription("Quiz actualizado");
        quiz = quizRepository.save(quiz);
        Optional<CourseQuiz> found = quizRepository.findById(quiz.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isEqualTo("Quiz actualizado");
        System.out.println("✅ Test 'Actualizar un quiz' pasó correctamente.");
    }

    /**
     * Este test verifica que se puede eliminar un quiz existente.
     */
    @Test
    @DisplayName("Eliminar un quiz")
    void testDelete() {
        CourseQuiz quiz = new CourseQuiz();
        quiz.setTitle("Quiz 1");
        quiz.setDescription("Primer quiz");
        quiz.setQuizType("AUTOEVALUACION");
        quiz.setCreatedAt(java.time.Instant.now());
        quiz.setCourseId(1);
        quiz = quizRepository.save(quiz);
        quizRepository.deleteById(quiz.getId());
        Optional<CourseQuiz> found = quizRepository.findById(quiz.getId());
        assertThat(found).isNotPresent();
        System.out.println("✅ Test 'Eliminar un quiz' pasó correctamente.");
    }
} 