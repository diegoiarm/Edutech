package com.edutech.courses.repository;

import com.edutech.courses.entity.CourseCategory;
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
class CourseCategoryRepositoryTest {
    @Autowired
    private CourseCategoryRepository categoryRepository;

    /**
     * Este test verifica que se puede guardar una categoría de curso en la base de datos
     * y luego recuperarla correctamente usando su ID. 
     * Se asegura que los campos 'name' y 'description' se persisten y recuperan sin errores.
     */
    @Test
    @DisplayName("Guardar y buscar una categoría por ID")
    void testSaveAndFindById() {
        CourseCategory category = new CourseCategory();
        category.setName("Ciencias");
        category.setDescription("Categoría de ciencias");
        category = categoryRepository.save(category);

        Optional<CourseCategory> found = categoryRepository.findById(category.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ciencias");
        assertThat(found.get().getDescription()).isEqualTo("Categoría de ciencias");

        // Mensaje de éxito en consola
        System.out.println("✅ Test 'Guardar y buscar una categoría por ID' pasó correctamente.");
    }

    /**
     * Este test verifica que se pueden recuperar todas las categorías guardadas en la base de datos.
     */
    @Test
    @DisplayName("Buscar todas las categorías")
    void testFindAll() {
        CourseCategory cat1 = new CourseCategory();
        cat1.setName("Ciencias");
        cat1.setDescription("Categoría de ciencias");
        categoryRepository.save(cat1);
        CourseCategory cat2 = new CourseCategory();
        cat2.setName("Artes");
        cat2.setDescription("Categoría de artes");
        categoryRepository.save(cat2);
        List<CourseCategory> all = categoryRepository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
        System.out.println("✅ Test 'Buscar todas las categorías' pasó correctamente.");
    }

    /**
     * Este test verifica que se puede actualizar una categoría existente.
     */
    @Test
    @DisplayName("Actualizar una categoría")
    void testUpdate() {
        CourseCategory category = new CourseCategory();
        category.setName("Ciencias");
        category.setDescription("Categoría de ciencias");
        category = categoryRepository.save(category);
        category.setDescription("Ciencias exactas");
        category = categoryRepository.save(category);
        Optional<CourseCategory> found = categoryRepository.findById(category.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isEqualTo("Ciencias exactas");
        System.out.println("✅ Test 'Actualizar una categoría' pasó correctamente.");
    }

    /**
     * Este test verifica que se puede eliminar una categoría existente.
     */
    @Test
    @DisplayName("Eliminar una categoría")
    void testDelete() {
        CourseCategory category = new CourseCategory();
        category.setName("Ciencias");
        category.setDescription("Categoría de ciencias");
        category = categoryRepository.save(category);
        categoryRepository.deleteById(category.getId());
        Optional<CourseCategory> found = categoryRepository.findById(category.getId());
        assertThat(found).isNotPresent();
        System.out.println("✅ Test 'Eliminar una categoría' pasó correctamente.");
    }
} 