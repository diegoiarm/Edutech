package com.edutech.courses.service;

import com.edutech.courses.entity.CourseCategory;
import com.edutech.courses.repository.CourseCategoryRepository;
import com.edutech.courses.mapper.CourseCategoryMapper;
import com.edutech.common.dto.CourseCategoryDTO;
import com.edutech.courses.entity.Course;
import com.edutech.courses.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class CourseCategoryServiceTest {
    @Mock
    private CourseCategoryRepository categoryRepository;
    @Mock
    private CourseCategoryMapper categoryMapper;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseCategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debería retornar una categoría por ID")
    void testFindById() {
        CourseCategory cat = new CourseCategory();
        cat.setId(1);
        cat.setName("Ciencias");
        cat.setDescription("Desc");
        CourseCategoryDTO dto = new CourseCategoryDTO();
        dto.setId(1);
        dto.setName("Ciencias");
        dto.setDescription("Desc");
        when(categoryRepository.findById(1)).thenReturn(Optional.of(cat));
        when(categoryMapper.toDTO(cat)).thenReturn(dto);
        CourseCategoryDTO result = categoryService.findById(1);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Ciencias");
        verify(categoryRepository).findById(1);
        System.out.println("✅ testFindById en CourseCategoryService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería eliminar una categoría por ID")
    void testDeleteById() {
        categoryService.deleteById(1);
        verify(categoryRepository).deleteById(1);
        System.out.println("✅ testDeleteById en CourseCategoryService pasó correctamente.");
    }

    @Test
    @DisplayName("findCategoryByCourseId retorna la categoría asociada a un curso")
    void testFindCategoryByCourseId() {
        Course course = new Course();
        course.setId(1);
        course.setCategoryId(2);
        CourseCategory category = new CourseCategory();
        category.setId(2);
        category.setName("Ciencia");
        category.setDescription("Desc");
        CourseCategoryDTO dto = new CourseCategoryDTO();
        dto.setId(2);
        dto.setName("Ciencia");
        dto.setDescription("Desc");

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(dto);

        CourseCategoryDTO result = categoryService.findCategoryByCourseId(1);
        assertEquals(2, result.getId());
        assertEquals("Ciencia", result.getName());
    }

    @Test
    @DisplayName("createForCourse crea la categoría y la asocia al curso")
    void testCreateForCourse() {
        Course course = new Course();
        course.setId(1);
        course.setCategoryId(null);
        CourseCategory category = new CourseCategory();
        category.setId(null);
        category.setName("Historia");
        category.setDescription("Desc");
        CourseCategory savedCategory = new CourseCategory();
        savedCategory.setId(5);
        savedCategory.setName("Historia");
        savedCategory.setDescription("Desc");
        CourseCategoryDTO dto = new CourseCategoryDTO();
        dto.setId(5);
        dto.setName("Historia");
        dto.setDescription("Desc");

        when(categoryMapper.toEntity(any(CourseCategoryDTO.class))).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(categoryMapper.toDTO(savedCategory)).thenReturn(dto);

        CourseCategoryDTO result = categoryService.createForCourse(1, dto);
        assertEquals(5, result.getId());
        assertEquals("Historia", result.getName());
        verify(courseRepository).save(any(Course.class));
    }
} 