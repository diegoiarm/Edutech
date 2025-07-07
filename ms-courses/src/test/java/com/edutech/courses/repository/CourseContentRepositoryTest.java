package com.edutech.courses.repository;

import com.edutech.courses.entity.CourseContent;
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
class CourseContentRepositoryTest {
    @Autowired
    private CourseContentRepository contentRepository;

    /**
     * Este test verifica que se puede guardar un contenido de curso en la base de datos
     * y luego recuperarlo correctamente usando su ID.
     * Se asegura que los campos obligatorios se persisten y recuperan sin errores.
     */
    @Test
    @DisplayName("Guardar y buscar un contenido por ID")
    void testSaveAndFindById() {
        CourseContent content = new CourseContent();
        content.setTitle("Introducción");
        content.setContentType("VIDEO");
        content.setUrl("https://ejemplo.com/video");
        content.setOrderIndex(1);
        content.setCourseId(1);
        content = contentRepository.save(content);
        Optional<CourseContent> found = contentRepository.findById(content.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Introducción");
        assertThat(found.get().getContentType()).isEqualTo("VIDEO");
        assertThat(found.get().getUrl()).isEqualTo("https://ejemplo.com/video");
        assertThat(found.get().getOrderIndex()).isEqualTo(1);
        assertThat(found.get().getCourseId()).isEqualTo(1);
        // Mensaje de éxito en consola
        System.out.println("✅ Test 'Guardar y buscar un contenido por ID' pasó correctamente.");
    }

    /**
     * Este test verifica que se pueden recuperar todos los contenidos guardados en la base de datos.
     */
    @Test
    @DisplayName("Buscar todos los contenidos")
    void testFindAll() {
        CourseContent c1 = new CourseContent();
        c1.setTitle("Introducción");
        c1.setContentType("VIDEO");
        c1.setUrl("https://ejemplo.com/video1");
        c1.setOrderIndex(1);
        c1.setCourseId(1);
        contentRepository.save(c1);
        CourseContent c2 = new CourseContent();
        c2.setTitle("Teoría");
        c2.setContentType("PDF");
        c2.setUrl("https://ejemplo.com/pdf");
        c2.setOrderIndex(2);
        c2.setCourseId(1);
        contentRepository.save(c2);
        List<CourseContent> all = contentRepository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
        System.out.println("✅ Test 'Buscar todos los contenidos' pasó correctamente.");
    }

    /**
     * Este test verifica que se puede actualizar un contenido existente.
     */
    @Test
    @DisplayName("Actualizar un contenido")
    void testUpdate() {
        CourseContent content = new CourseContent();
        content.setTitle("Introducción");
        content.setContentType("VIDEO");
        content.setUrl("https://ejemplo.com/video");
        content.setOrderIndex(1);
        content.setCourseId(1);
        content = contentRepository.save(content);
        content.setTitle("Introducción actualizada");
        content = contentRepository.save(content);
        Optional<CourseContent> found = contentRepository.findById(content.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Introducción actualizada");
        System.out.println("✅ Test 'Actualizar un contenido' pasó correctamente.");
    }

    /**
     * Este test verifica que se puede eliminar un contenido existente.
     */
    @Test
    @DisplayName("Eliminar un contenido")
    void testDelete() {
        CourseContent content = new CourseContent();
        content.setTitle("Introducción");
        content.setContentType("VIDEO");
        content.setUrl("https://ejemplo.com/video");
        content.setOrderIndex(1);
        content.setCourseId(1);
        content = contentRepository.save(content);
        contentRepository.deleteById(content.getId());
        Optional<CourseContent> found = contentRepository.findById(content.getId());
        assertThat(found).isNotPresent();
        System.out.println("✅ Test 'Eliminar un contenido' pasó correctamente.");
    }
} 