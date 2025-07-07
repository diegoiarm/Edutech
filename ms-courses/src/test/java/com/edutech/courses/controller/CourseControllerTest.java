package com.edutech.courses.controller;

import com.edutech.common.dto.CourseDTO;
import com.edutech.courses.service.CourseService;
import com.edutech.courses.assembler.CourseAssembler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Import(CourseAssembler.class)
class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    @DisplayName("GET /api/courses debe retornar lista de cursos con enlaces HATEOAS")
    void testFindAll() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setId(1);
        dto.setTitle("Matemáticas");
        when(courseService.findAll()).thenReturn(Arrays.asList(dto));
        
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.courseDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.courseDTOList[0].title").value("Matemáticas"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.courseDTOList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.courseDTOList[0]._links.update.href").exists())
                .andExpect(jsonPath("$._embedded.courseDTOList[0]._links.delete.href").exists())
                .andExpect(jsonPath("$._embedded.courseDTOList[0]._links.enroll.href").exists());
    }

    @Test
    @DisplayName("GET /api/courses/{id} debe retornar un curso con enlaces HATEOAS")
    void testFindById() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setId(1);
        dto.setTitle("Matemáticas");
        when(courseService.findById(1)).thenReturn(dto);
        
        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Matemáticas"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists())
                .andExpect(jsonPath("$._links.enroll.href").exists())
                .andExpect(jsonPath("$._links.content.href").exists())
                .andExpect(jsonPath("$._links.comments.href").exists());
    }

    @Test
    @DisplayName("POST /api/courses debe crear un curso")
    void testCreate() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setId(1);
        dto.setTitle("Matemáticas");
        when(courseService.create(any(CourseDTO.class))).thenReturn(dto);
        
        String json = "{" +
                "\"title\":\"Matemáticas\"," +
                "\"description\":\"desc\"," +
                "\"categoryId\":1," +
                "\"managerId\":1," +
                "\"instructorId\":1," +
                "\"publishDate\":\"2024-01-01\"," +
                "\"price\":100," +
                "\"image\":\"img.png\"," +
                "\"status\":\"ACTIVO\"}";
        
        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Matemáticas"));
    }

    @Test
    @DisplayName("PUT /api/courses/{id} debe actualizar un curso")
    void testUpdate() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setId(1);
        dto.setTitle("Matemáticas");
        when(courseService.update(eq(1), any(CourseDTO.class))).thenReturn(dto);
        
        String json = "{" +
                "\"title\":\"Matemáticas\"," +
                "\"description\":\"desc\"," +
                "\"categoryId\":1," +
                "\"managerId\":1," +
                "\"instructorId\":1," +
                "\"publishDate\":\"2024-01-01\"," +
                "\"price\":100," +
                "\"image\":\"img.png\"," +
                "\"status\":\"ACTIVO\"}";
        
        mockMvc.perform(put("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Matemáticas"));
    }

    @Test
    @DisplayName("DELETE /api/courses/{id} debe eliminar un curso")
    void testDelete() throws Exception {
        Mockito.doNothing().when(courseService).delete(1);
        
        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());
    }
} 