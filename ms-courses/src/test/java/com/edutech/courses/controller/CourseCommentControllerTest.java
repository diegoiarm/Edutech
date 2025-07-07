package com.edutech.courses.controller;

import com.edutech.common.dto.CourseCommentDTO;
import com.edutech.courses.assembler.CourseCommentAssembler;
import com.edutech.courses.service.CourseCommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({CourseCommentController.class, CourseCommentByCourseController.class})
@Import({CourseCommentAssembler.class})
class CourseCommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCommentService commentService;

    @Test
    @DisplayName("GET /api/course-comments debe retornar lista de comentarios con enlaces HATEOAS")
    void testFindAll() throws Exception {
        CourseCommentDTO dto = new CourseCommentDTO();
        dto.setId(1);
        dto.setCourseId(10);
        dto.setUserId(5);
        dto.setCommentText("Excelente curso");
        dto.setRating(5);
        dto.setCreatedAt(Instant.now());
        when(commentService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/course-comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.courseCommentDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.courseCommentDTOList[0].commentText").value("Excelente curso"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.courseCommentDTOList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.courseCommentDTOList[0]._links.update.href").exists())
                .andExpect(jsonPath("$._embedded.courseCommentDTOList[0]._links.delete.href").exists());
    }

    @Test
    @DisplayName("GET /api/course-comments/{id} debe retornar un comentario con enlaces HATEOAS")
    void testFindById() throws Exception {
        CourseCommentDTO dto = new CourseCommentDTO();
        dto.setId(1);
        dto.setCourseId(10);
        dto.setUserId(5);
        dto.setCommentText("Excelente curso");
        dto.setRating(5);
        dto.setCreatedAt(Instant.now());
        when(commentService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/course-comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.commentText").value("Excelente curso"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());
    }

    @Test
    @DisplayName("POST /api/course-comments debe crear un comentario")
    void testCreate() throws Exception {
        CourseCommentDTO dto = new CourseCommentDTO();
        dto.setId(1);
        dto.setCourseId(10);
        dto.setUserId(5);
        dto.setCommentText("Excelente curso");
        dto.setRating(5);
        dto.setCreatedAt(Instant.now());
        when(commentService.create(any(CourseCommentDTO.class))).thenReturn(dto);

        String json = "{" +
                "\"courseId\":10," +
                "\"userId\":5," +
                "\"commentText\":\"Excelente curso\"," +
                "\"rating\":5}";

        mockMvc.perform(post("/api/course-comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.commentText").value("Excelente curso"));
    }

    @Test
    @DisplayName("PUT /api/course-comments/{id} debe actualizar un comentario")
    void testUpdate() throws Exception {
        CourseCommentDTO dto = new CourseCommentDTO();
        dto.setId(1);
        dto.setCourseId(10);
        dto.setUserId(5);
        dto.setCommentText("Comentario actualizado");
        dto.setRating(4);
        dto.setCreatedAt(Instant.now());
        when(commentService.update(eq(1), any(CourseCommentDTO.class))).thenReturn(dto);

        String json = "{" +
                "\"courseId\":10," +
                "\"userId\":5," +
                "\"commentText\":\"Comentario actualizado\"," +
                "\"rating\":4}";

        mockMvc.perform(put("/api/course-comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.commentText").value("Comentario actualizado"));
    }

    @Test
    @DisplayName("DELETE /api/course-comments/{id} debe eliminar un comentario")
    void testDelete() throws Exception {
        Mockito.doNothing().when(commentService).delete(1);

        mockMvc.perform(delete("/api/course-comments/1"))
                .andExpect(status().isNoContent());
    }

    // --- Endpoints por curso ---

    @Test
    @DisplayName("GET /api/courses/{courseId}/comments debe retornar comentarios asociados a un curso")
    void testFindCommentsByCourseId() throws Exception {
        CourseCommentDTO dto1 = new CourseCommentDTO();
        dto1.setId(1);
        dto1.setCourseId(10);
        dto1.setUserId(5);
        dto1.setCommentText("Excelente curso");
        dto1.setRating(5);
        dto1.setCreatedAt(Instant.now());

        CourseCommentDTO dto2 = new CourseCommentDTO();
        dto2.setId(2);
        dto2.setCourseId(10);
        dto2.setUserId(6);
        dto2.setCommentText("Muy bueno");
        dto2.setRating(4);
        dto2.setCreatedAt(Instant.now());

        when(commentService.findCommentsByCourseId(10)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/courses/10/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.courseCommentDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.courseCommentDTOList[0].commentText").value("Excelente curso"))
                .andExpect(jsonPath("$._embedded.courseCommentDTOList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.courseCommentDTOList[1].commentText").value("Muy bueno"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("POST /api/courses/{courseId}/comments debe crear y asociar un comentario a un curso")
    void testCreateForCourse() throws Exception {
        CourseCommentDTO dto = new CourseCommentDTO();
        dto.setId(3);
        dto.setCourseId(10);
        dto.setUserId(7);
        dto.setCommentText("Nuevo comentario");
        dto.setRating(3);
        dto.setCreatedAt(Instant.now());
        when(commentService.createForCourse(eq(10), any(CourseCommentDTO.class))).thenReturn(dto);

        String json = "{" +
                "\"courseId\":10," +
                "\"userId\":7," +
                "\"commentText\":\"Nuevo comentario\"," +
                "\"rating\":3}";

        mockMvc.perform(post("/api/courses/10/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.commentText").value("Nuevo comentario"))
                .andExpect(jsonPath("$.courseId").value(10));
    }
} 