package com.edutech.courses.service;

import com.edutech.courses.repository.CourseQuizRepository;
import com.edutech.courses.entity.CourseQuiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseQuizServiceTest {
    @Mock
    private CourseQuizRepository quizRepository;

    @InjectMocks
    private CourseQuizService quizService;

    @BeforeEach
    void setUp() {
        // Configuración inicial si es necesaria
    }

    @Test
    @DisplayName("Debería retornar todos los quizzes")
    void testFindAll() {
        CourseQuiz q1 = new CourseQuiz();
        q1.setId(1);
        q1.setTitle("Quiz 1");
        CourseQuiz q2 = new CourseQuiz();
        q2.setId(2);
        q2.setTitle("Quiz 2");
        when(quizRepository.findAll()).thenReturn(Arrays.asList(q1, q2));
        List<CourseQuiz> result = quizService.findAll();
        assertThat(result).hasSize(2);
        verify(quizRepository).findAll();
        System.out.println("✅ testFindAll en CourseQuizService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería retornar un quiz por ID")
    void testFindById() {
        CourseQuiz q = new CourseQuiz();
        q.setId(1);
        q.setTitle("Quiz 1");
        when(quizRepository.findById(1)).thenReturn(Optional.of(q));
        Optional<CourseQuiz> result = quizService.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Quiz 1");
        verify(quizRepository).findById(1);
        System.out.println("✅ testFindById en CourseQuizService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería guardar un quiz")
    void testSave() {
        CourseQuiz q = new CourseQuiz();
        q.setTitle("Quiz 1");
        when(quizRepository.save(q)).thenReturn(q);
        CourseQuiz result = quizService.save(q);
        assertThat(result.getTitle()).isEqualTo("Quiz 1");
        verify(quizRepository).save(q);
        System.out.println("✅ testSave en CourseQuizService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería eliminar un quiz por ID")
    void testDeleteById() {
        quizService.deleteById(1);
        verify(quizRepository).deleteById(1);
        System.out.println("✅ testDeleteById en CourseQuizService pasó correctamente.");
    }
} 