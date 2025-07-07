package com.edutech.courses.service;

import com.edutech.courses.repository.CourseContentRepository;
import com.edutech.courses.entity.CourseContent;
import com.edutech.courses.mapper.CourseContentMapper;
import com.edutech.common.dto.CourseContentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseContentServiceTest {
    @Mock
    private CourseContentRepository contentRepository;
    @Mock
    private CourseContentMapper contentMapper;

    @InjectMocks
    private CourseContentService contentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debería retornar todas las categorías")
    void testFindAll() {
        CourseContent content1 = new CourseContent();
        content1.setId(1);
        content1.setCourseId(10);
        content1.setTitle("Introducción");
        content1.setContentType("VIDEO");
        content1.setUrl("https://example.com/video1");
        content1.setOrderIndex(1);

        CourseContent content2 = new CourseContent();
        content2.setId(2);
        content2.setCourseId(10);
        content2.setTitle("Primera lección");
        content2.setContentType("PDF");
        content2.setUrl("https://example.com/pdf1");
        content2.setOrderIndex(2);

        CourseContentDTO dto1 = new CourseContentDTO();
        dto1.setId(1);
        dto1.setCourseId(10);
        dto1.setTitle("Introducción");
        dto1.setContentType("VIDEO");
        dto1.setUrl("https://example.com/video1");
        dto1.setOrderIndex(1);

        CourseContentDTO dto2 = new CourseContentDTO();
        dto2.setId(2);
        dto2.setCourseId(10);
        dto2.setTitle("Primera lección");
        dto2.setContentType("PDF");
        dto2.setUrl("https://example.com/pdf1");
        dto2.setOrderIndex(2);

        when(contentRepository.findAll()).thenReturn(Arrays.asList(content1, content2));
        when(contentMapper.toDTO(content1)).thenReturn(dto1);
        when(contentMapper.toDTO(content2)).thenReturn(dto2);

        List<CourseContentDTO> result = contentService.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Introducción");
        assertThat(result.get(1).getTitle()).isEqualTo("Primera lección");
        verify(contentRepository).findAll();
        System.out.println("✅ testFindAll en CourseContentService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería retornar una categoría por ID")
    void testFindById() {
        CourseContent content = new CourseContent();
        content.setId(1);
        content.setCourseId(10);
        content.setTitle("Introducción");
        content.setContentType("VIDEO");
        content.setUrl("https://example.com/video1");
        content.setOrderIndex(1);

        CourseContentDTO dto = new CourseContentDTO();
        dto.setId(1);
        dto.setCourseId(10);
        dto.setTitle("Introducción");
        dto.setContentType("VIDEO");
        dto.setUrl("https://example.com/video1");
        dto.setOrderIndex(1);

        when(contentRepository.findById(1)).thenReturn(Optional.of(content));
        when(contentMapper.toDTO(content)).thenReturn(dto);

        CourseContentDTO result = contentService.findById(1);
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Introducción");
        verify(contentRepository).findById(1);
        System.out.println("✅ testFindById en CourseContentService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería guardar una categoría")
    void testSave() {
        CourseContent content = new CourseContent();
        content.setCourseId(10);
        content.setTitle("Introducción");
        content.setContentType("VIDEO");
        content.setUrl("https://example.com/video1");
        content.setOrderIndex(1);

        when(contentRepository.save(content)).thenReturn(content);
        CourseContent result = contentService.save(content);
        assertThat(result.getTitle()).isEqualTo("Introducción");
        verify(contentRepository).save(content);
        System.out.println("✅ testSave en CourseContentService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería eliminar una categoría por ID")
    void testDeleteById() {
        contentService.deleteById(1);
        verify(contentRepository).deleteById(1);
        System.out.println("✅ testDeleteById en CourseContentService pasó correctamente.");
    }

    @Test
    @DisplayName("findContentsByCourseId retorna los contenidos asociados a un curso")
    void testFindContentsByCourseId() {
        CourseContent content1 = new CourseContent();
        content1.setId(1);
        content1.setCourseId(10);
        content1.setTitle("Introducción");
        content1.setContentType("VIDEO");
        content1.setUrl("https://example.com/video1");
        content1.setOrderIndex(1);

        CourseContent content2 = new CourseContent();
        content2.setId(2);
        content2.setCourseId(10);
        content2.setTitle("Primera lección");
        content2.setContentType("PDF");
        content2.setUrl("https://example.com/pdf1");
        content2.setOrderIndex(2);

        CourseContentDTO dto1 = new CourseContentDTO();
        dto1.setId(1);
        dto1.setCourseId(10);
        dto1.setTitle("Introducción");
        dto1.setContentType("VIDEO");
        dto1.setUrl("https://example.com/video1");
        dto1.setOrderIndex(1);

        CourseContentDTO dto2 = new CourseContentDTO();
        dto2.setId(2);
        dto2.setCourseId(10);
        dto2.setTitle("Primera lección");
        dto2.setContentType("PDF");
        dto2.setUrl("https://example.com/pdf1");
        dto2.setOrderIndex(2);

        when(contentRepository.findByCourseId(10)).thenReturn(Arrays.asList(content1, content2));
        when(contentMapper.toDTO(content1)).thenReturn(dto1);
        when(contentMapper.toDTO(content2)).thenReturn(dto2);

        List<CourseContentDTO> result = contentService.findContentsByCourseId(10);
        assertEquals(2, result.size());
        assertEquals("Introducción", result.get(0).getTitle());
        assertEquals("Primera lección", result.get(1).getTitle());
    }

    @Test
    @DisplayName("createForCourse crea el contenido y lo asocia al curso")
    void testCreateForCourse() {
        CourseContentDTO inputDto = new CourseContentDTO();
        inputDto.setTitle("Nueva lección");
        inputDto.setContentType("VIDEO");
        inputDto.setUrl("https://example.com/video3");
        inputDto.setOrderIndex(3);

        CourseContent content = new CourseContent();
        content.setId(null);
        content.setCourseId(10);
        content.setTitle("Nueva lección");
        content.setContentType("VIDEO");
        content.setUrl("https://example.com/video3");
        content.setOrderIndex(3);

        CourseContent savedContent = new CourseContent();
        savedContent.setId(3);
        savedContent.setCourseId(10);
        savedContent.setTitle("Nueva lección");
        savedContent.setContentType("VIDEO");
        savedContent.setUrl("https://example.com/video3");
        savedContent.setOrderIndex(3);

        CourseContentDTO resultDto = new CourseContentDTO();
        resultDto.setId(3);
        resultDto.setCourseId(10);
        resultDto.setTitle("Nueva lección");
        resultDto.setContentType("VIDEO");
        resultDto.setUrl("https://example.com/video3");
        resultDto.setOrderIndex(3);

        when(contentMapper.toEntity(any(CourseContentDTO.class))).thenReturn(content);
        when(contentRepository.save(content)).thenReturn(savedContent);
        when(contentMapper.toDTO(savedContent)).thenReturn(resultDto);

        CourseContentDTO result = contentService.createForCourse(10, inputDto);
        assertEquals(3, result.getId());
        assertEquals("Nueva lección", result.getTitle());
        assertEquals(10, result.getCourseId());
    }
} 