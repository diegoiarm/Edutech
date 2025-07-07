package com.edutech.courses.controller;

import com.edutech.common.dto.CourseContentDTO;
import com.edutech.courses.assembler.CourseContentAssembler;
import com.edutech.courses.service.CourseContentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({CourseContentController.class, CourseContentByCourseController.class})
@Import({CourseContentAssembler.class})
class CourseContentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseContentService contentService;

    @Test
    @DisplayName("GET /api/course-contents debe retornar lista de contenidos con enlaces HATEOAS")
    void testFindAll() throws Exception {
        CourseContentDTO dto = new CourseContentDTO();
        dto.setId(1);
        dto.setCourseId(10);
        dto.setTitle("Introducción al curso");
        dto.setContentType("VIDEO");
        dto.setUrl("https://example.com/video1");
        dto.setOrderIndex(1);
        when(contentService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/course-contents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.courseContentDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.courseContentDTOList[0].title").value("Introducción al curso"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.courseContentDTOList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.courseContentDTOList[0]._links.update.href").exists())
                .andExpect(jsonPath("$._embedded.courseContentDTOList[0]._links.delete.href").exists());
    }

    @Test
    @DisplayName("GET /api/course-contents/{id} debe retornar un contenido con enlaces HATEOAS")
    void testFindById() throws Exception {
        CourseContentDTO dto = new CourseContentDTO();
        dto.setId(1);
        dto.setCourseId(10);
        dto.setTitle("Introducción al curso");
        dto.setContentType("VIDEO");
        dto.setUrl("https://example.com/video1");
        dto.setOrderIndex(1);
        when(contentService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/course-contents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Introducción al curso"))
                .andExpect(jsonPath("$.contentType").value("VIDEO"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());
    }

    @Test
    @DisplayName("POST /api/course-contents debe crear un contenido")
    void testCreate() throws Exception {
        CourseContentDTO dto = new CourseContentDTO();
        dto.setId(1);
        dto.setCourseId(10);
        dto.setTitle("Introducción al curso");
        dto.setContentType("VIDEO");
        dto.setUrl("https://example.com/video1");
        dto.setOrderIndex(1);
        when(contentService.create(any(CourseContentDTO.class))).thenReturn(dto);

        String json = "{" +
                "\"courseId\":10," +
                "\"title\":\"Introducción al curso\"," +
                "\"contentType\":\"VIDEO\"," +
                "\"url\":\"https://example.com/video1\"," +
                "\"orderIndex\":1}";

        mockMvc.perform(post("/api/course-contents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Introducción al curso"));
    }

    @Test
    @DisplayName("PUT /api/course-contents/{id} debe actualizar un contenido")
    void testUpdate() throws Exception {
        CourseContentDTO dto = new CourseContentDTO();
        dto.setId(1);
        dto.setCourseId(10);
        dto.setTitle("Introducción actualizada");
        dto.setContentType("VIDEO");
        dto.setUrl("https://example.com/video1");
        dto.setOrderIndex(1);
        when(contentService.update(eq(1), any(CourseContentDTO.class))).thenReturn(dto);

        String json = "{" +
                "\"courseId\":10," +
                "\"title\":\"Introducción actualizada\"," +
                "\"contentType\":\"VIDEO\"," +
                "\"url\":\"https://example.com/video1\"," +
                "\"orderIndex\":1}";

        mockMvc.perform(put("/api/course-contents/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Introducción actualizada"));
    }

    @Test
    @DisplayName("DELETE /api/course-contents/{id} debe eliminar un contenido")
    void testDelete() throws Exception {
        Mockito.doNothing().when(contentService).delete(1);

        mockMvc.perform(delete("/api/course-contents/1"))
                .andExpect(status().isNoContent());
    }

    // --- Endpoints por curso ---

    @Test
    @DisplayName("GET /api/courses/{courseId}/contents debe retornar contenidos asociados a un curso")
    void testFindContentsByCourseId() throws Exception {
        CourseContentDTO dto1 = new CourseContentDTO();
        dto1.setId(1);
        dto1.setCourseId(10);
        dto1.setTitle("Introducción al curso");
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

        when(contentService.findContentsByCourseId(10)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/courses/10/contents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.courseContentDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.courseContentDTOList[0].title").value("Introducción al curso"))
                .andExpect(jsonPath("$._embedded.courseContentDTOList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.courseContentDTOList[1].title").value("Primera lección"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("POST /api/courses/{courseId}/contents debe crear y asociar un contenido a un curso")
    void testCreateForCourse() throws Exception {
        CourseContentDTO dto = new CourseContentDTO();
        dto.setId(3);
        dto.setCourseId(10);
        dto.setTitle("Nueva lección");
        dto.setContentType("VIDEO");
        dto.setUrl("https://example.com/video3");
        dto.setOrderIndex(3);
        when(contentService.createForCourse(eq(10), any(CourseContentDTO.class))).thenReturn(dto);

        String json = "{" +
                "\"courseId\":10," +
                "\"title\":\"Nueva lección\"," +
                "\"contentType\":\"VIDEO\"," +
                "\"url\":\"https://example.com/video3\"," +
                "\"orderIndex\":3}";

        mockMvc.perform(post("/api/courses/10/contents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Nueva lección"))
                .andExpect(jsonPath("$.courseId").value(10));
    }
} 