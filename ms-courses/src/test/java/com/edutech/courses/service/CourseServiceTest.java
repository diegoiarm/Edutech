package com.edutech.courses.service;

import com.edutech.courses.repository.CourseRepository;
import com.edutech.courses.repository.CourseCategoryRepository;
import com.edutech.courses.mapper.CourseMapper;
import com.edutech.courses.client.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.edutech.courses.entity.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import com.edutech.common.dto.CourseDTO;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepo;
    @Mock
    private CourseCategoryRepository categRepo;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private UserClient userClient;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        // Configuración inicial si es necesaria
    }

    @Test
    @DisplayName("Debería retornar todos los cursos")
    void testFindAll() {
        Course c1 = new Course();
        c1.setId(1);
        c1.setTitle("Matemáticas");
        Course c2 = new Course();
        c2.setId(2);
        c2.setTitle("Física");
        when(courseRepo.findAll()).thenReturn(Arrays.asList(c1, c2));
        List<CourseDTO> result = courseService.findAll();
        assertThat(result).hasSize(2);
        verify(courseRepo).findAll();
        System.out.println("✅ testFindAll en CourseService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería retornar un curso por ID")
    void testFindById() {
        Course c = new Course();
        c.setId(1);
        c.setTitle("Matemáticas");
        when(courseRepo.findById(1)).thenReturn(Optional.of(c));
        CourseDTO dto = new CourseDTO();
        dto.setId(1);
        dto.setTitle("Matemáticas");
        when(courseMapper.toDTO(c)).thenReturn(dto);
        CourseDTO result = courseService.findById(1);
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Matemáticas");
        verify(courseRepo).findById(1);
        System.out.println("✅ testFindById en CourseService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería guardar un curso")
    void testSave() {
        Course c = new Course();
        c.setTitle("Matemáticas");
        when(courseRepo.save(c)).thenReturn(c);
        CourseDTO dto = new CourseDTO();
        dto.setTitle("Matemáticas");
        when(courseMapper.toDTO(c)).thenReturn(dto);
        CourseDTO result = courseService.save(c);
        assertThat(result.getTitle()).isEqualTo("Matemáticas");
        verify(courseRepo).save(c);
        System.out.println("✅ testSave en CourseService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería eliminar un curso por ID")
    void testDeleteById() {
        courseService.deleteById(1);
        verify(courseRepo).deleteById(1);
        System.out.println("✅ testDeleteById en CourseService pasó correctamente.");
    }

    // Aquí irán los tests unitarios
} 