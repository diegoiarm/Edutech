package com.edutech.courses.repository;

import com.edutech.courses.entity.Course;
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
class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;

    /**
     * Este test verifica que se puede guardar un curso en la base de datos
     * y luego recuperarlo correctamente usando su ID.
     * Se asegura que los campos obligatorios se persisten y recuperan sin errores.
     */
    @Test
    @DisplayName("Guardar y buscar un curso por ID")
    void testSaveAndFindById() {
        Course course = new Course();
        course.setTitle("Matemáticas");
        course.setDescription("Curso de matemáticas básicas");
        course.setCategoryId(1);
        course.setManagerId(1);
        course.setInstructorId(1);
        course.setPublishDate(java.time.LocalDate.now());
        course.setPrice(java.math.BigDecimal.TEN);
        course.setImage("img.png");
        course.setStatus("ACTIVO");
        course = courseRepository.save(course);
        Optional<Course> found = courseRepository.findById(course.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Matemáticas");
        // Mensaje de éxito en consola
        System.out.println("✅ Test 'Guardar y buscar un curso por ID' pasó correctamente.");
    }

    /**
     * Este test verifica que se pueden recuperar todos los cursos guardados en la base de datos.
     */
    @Test
    @DisplayName("Buscar todos los cursos")
    void testFindAll() {
        Course c1 = new Course();
        c1.setTitle("Matemáticas");
        c1.setDescription("Curso de matemáticas");
        c1.setCategoryId(1);
        c1.setManagerId(1);
        c1.setInstructorId(1);
        c1.setPublishDate(java.time.LocalDate.now());
        c1.setPrice(java.math.BigDecimal.TEN);
        c1.setImage("img.png");
        c1.setStatus("ACTIVO");
        courseRepository.save(c1);
        Course c2 = new Course();
        c2.setTitle("Física");
        c2.setDescription("Curso de física");
        c2.setCategoryId(1);
        c2.setManagerId(1);
        c2.setInstructorId(1);
        c2.setPublishDate(java.time.LocalDate.now());
        c2.setPrice(java.math.BigDecimal.TEN);
        c2.setImage("img2.png");
        c2.setStatus("ACTIVO");
        courseRepository.save(c2);
        List<Course> all = courseRepository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
        System.out.println("✅ Test 'Buscar todos los cursos' pasó correctamente.");
    }

    /**
     * Este test verifica que se puede actualizar un curso existente.
     */
    @Test
    @DisplayName("Actualizar un curso")
    void testUpdate() {
        Course course = new Course();
        course.setTitle("Matemáticas");
        course.setDescription("Curso de matemáticas");
        course.setCategoryId(1);
        course.setManagerId(1);
        course.setInstructorId(1);
        course.setPublishDate(java.time.LocalDate.now());
        course.setPrice(java.math.BigDecimal.TEN);
        course.setImage("img.png");
        course.setStatus("ACTIVO");
        course = courseRepository.save(course);
        course.setDescription("Curso de matemáticas avanzadas");
        course = courseRepository.save(course);
        Optional<Course> found = courseRepository.findById(course.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isEqualTo("Curso de matemáticas avanzadas");
        System.out.println("✅ Test 'Actualizar un curso' pasó correctamente.");
    }

    /**
     * Este test verifica que se puede eliminar un curso existente.
     */
    @Test
    @DisplayName("Eliminar un curso")
    void testDelete() {
        Course course = new Course();
        course.setTitle("Matemáticas");
        course.setDescription("Curso de matemáticas");
        course.setCategoryId(1);
        course.setManagerId(1);
        course.setInstructorId(1);
        course.setPublishDate(java.time.LocalDate.now());
        course.setPrice(java.math.BigDecimal.TEN);
        course.setImage("img.png");
        course.setStatus("ACTIVO");
        course = courseRepository.save(course);
        courseRepository.deleteById(course.getId());
        Optional<Course> found = courseRepository.findById(course.getId());
        assertThat(found).isNotPresent();
        System.out.println("✅ Test 'Eliminar un curso' pasó correctamente.");
    }
} 