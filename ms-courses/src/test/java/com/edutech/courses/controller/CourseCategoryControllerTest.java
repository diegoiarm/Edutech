package com.edutech.courses.controller;

import com.edutech.common.dto.CourseCategoryDTO;
import com.edutech.courses.assembler.CourseCategoryAssembler;
import com.edutech.courses.service.CourseCategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({CourseCategoryController.class, CourseCategoryByCourseController.class})
@Import({CourseCategoryAssembler.class})
class CourseCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCategoryService categService;

    @Test
    @DisplayName("GET /api/courses/{courseId}/categories debe retornar la categoría asociada a un curso")
    void testFindCategoryByCourseId() throws Exception {
        CourseCategoryDTO dto = new CourseCategoryDTO();
        dto.setId(2);
        dto.setName("Matemáticas");
        dto.setDescription("Desc");
        when(categService.findCategoryByCourseId(10)).thenReturn(dto);

        mockMvc.perform(get("/api/courses/10/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Matemáticas"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("GET /api/course-categories/{id} debe retornar una categoría con enlaces HATEOAS")
    void testFindById() throws Exception {
        CourseCategoryDTO dto = new CourseCategoryDTO();
        dto.setId(1);
        dto.setName("Ciencia");
        dto.setDescription("Descripción");
        when(categService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/course-categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ciencia"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());
    }

    @Test
    @DisplayName("POST /api/courses/{courseId}/categories debe crear y asociar una categoría a un curso")
    void testCreateForCourse() throws Exception {
        CourseCategoryDTO dto = new CourseCategoryDTO();
        dto.setId(3);
        dto.setName("Historia");
        dto.setDescription("Desc");
        when(categService.createForCourse(eq(5), any(CourseCategoryDTO.class))).thenReturn(dto);

        String json = "{" +
                "\"name\":\"Historia\"," +
                "\"description\":\"Desc\"}";

        mockMvc.perform(post("/api/courses/5/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Historia"));
    }

    @Test
    @DisplayName("PUT /api/course-categories/{id} debe actualizar una categoría")
    void testUpdate() throws Exception {
        CourseCategoryDTO dto = new CourseCategoryDTO();
        dto.setId(1);
        dto.setName("Ciencia");
        dto.setDescription("Descripción");
        when(categService.update(eq(1), any(CourseCategoryDTO.class))).thenReturn(dto);

        String json = "{" +
                "\"name\":\"Ciencia\"," +
                "\"description\":\"Descripción\"}";

        mockMvc.perform(put("/api/course-categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ciencia"));
    }

    @Test
    @DisplayName("DELETE /api/course-categories/{id} debe eliminar una categoría")
    void testDelete() throws Exception {
        Mockito.doNothing().when(categService).delete(1);

        mockMvc.perform(delete("/api/course-categories/1"))
                .andExpect(status().isNoContent());
    }
} 