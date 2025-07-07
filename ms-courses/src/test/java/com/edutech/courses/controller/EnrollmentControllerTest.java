package com.edutech.courses.controller;

import com.edutech.courses.entity.Enrollment;
import com.edutech.courses.mapper.EnrollmentMapper;
import com.edutech.courses.service.EnrollmentService;
import com.edutech.common.dto.EnrollmentDTO;
import com.edutech.courses.assembler.EnrollmentAssembler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({EnrollmentController.class, EnrollmentByCourseController.class})
@Import({EnrollmentAssembler.class})
class EnrollmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;
    
    @MockBean
    private EnrollmentMapper enrollmentMapper;

    @Test
    @DisplayName("GET /api/enrollments debe retornar lista de matrículas con enlaces HATEOAS")
    void testFindAll() throws Exception {
        Enrollment e = new Enrollment();
        e.setId(1);
        e.setStudentId(2);
        e.setCourseId(3);
        e.setEnrolledAt(Instant.now());
        e.setStatus("ACTIVO");
        
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(1);
        dto.setStudentId(2);
        dto.setCourseId(3);
        dto.setEnrolledAt(e.getEnrolledAt());
        dto.setStatus("ACTIVO");
        
        when(enrollmentService.findAll()).thenReturn(Arrays.asList(e));
        when(enrollmentMapper.toDTO(e)).thenReturn(dto);
        
        mockMvc.perform(get("/api/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0].studentId").value(2))
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0].courseId").value(3))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0]._links.update.href").exists())
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0]._links.delete.href").exists())
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0]._links.course.href").exists())
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0]._links.progress.href").exists())
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0]._links.certificate.href").exists());
    }

    @Test
    @DisplayName("GET /api/enrollments/{id} debe retornar una matrícula con enlaces HATEOAS")
    void testFindById() throws Exception {
        Enrollment e = new Enrollment();
        e.setId(1);
        e.setStudentId(2);
        e.setCourseId(3);
        e.setEnrolledAt(Instant.now());
        e.setStatus("ACTIVO");
        
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(1);
        dto.setStudentId(2);
        dto.setCourseId(3);
        dto.setEnrolledAt(e.getEnrolledAt());
        dto.setStatus("ACTIVO");
        
        when(enrollmentService.findById(1)).thenReturn(Optional.of(e));
        when(enrollmentMapper.toDTO(e)).thenReturn(dto);
        
        mockMvc.perform(get("/api/enrollments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentId").value(2))
                .andExpect(jsonPath("$.courseId").value(3))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists())
                .andExpect(jsonPath("$._links.course.href").exists())
                .andExpect(jsonPath("$._links.progress.href").exists())
                .andExpect(jsonPath("$._links.certificate.href").exists());
    }

    @Test
    @DisplayName("PUT /api/enrollments/{id} debe actualizar una inscripción")
    void testUpdate() throws Exception {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(1);
        dto.setStudentId(2);
        dto.setCourseId(3);
        dto.setEnrolledAt(Instant.now());
        dto.setStatus("ACTIVO");
        when(enrollmentService.findById(1)).thenReturn(Optional.of(new com.edutech.courses.entity.Enrollment()));
        when(enrollmentMapper.toEntity(any(EnrollmentDTO.class))).thenReturn(new com.edutech.courses.entity.Enrollment());
        when(enrollmentService.save(any())).thenReturn(new com.edutech.courses.entity.Enrollment());
        when(enrollmentMapper.toDTO(any())).thenReturn(dto);

        String json = "{" +
                "\"studentId\":2," +
                "\"courseId\":3," +
                "\"enrolledAt\":\"2024-01-01T00:00:00Z\"," +
                "\"status\":\"ACTIVO\"}";

        mockMvc.perform(put("/api/enrollments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentId").value(2));
    }

    @Test
    @DisplayName("DELETE /api/enrollments/{id} debe eliminar una inscripción")
    void testDelete() throws Exception {
        when(enrollmentService.findById(1)).thenReturn(Optional.of(new com.edutech.courses.entity.Enrollment()));
        Mockito.doNothing().when(enrollmentService).deleteById(1);

        mockMvc.perform(delete("/api/enrollments/1"))
                .andExpect(status().isNoContent());
    }

    // --- Endpoints por curso ---

    @Test
    @DisplayName("GET /api/courses/{courseId}/enrollments debe retornar inscripciones asociadas a un curso")
    void testFindByCourseId() throws Exception {
        EnrollmentDTO dto1 = new EnrollmentDTO();
        dto1.setId(1);
        dto1.setStudentId(2);
        dto1.setCourseId(10);
        dto1.setEnrolledAt(Instant.now());
        dto1.setStatus("ACTIVO");

        EnrollmentDTO dto2 = new EnrollmentDTO();
        dto2.setId(2);
        dto2.setStudentId(3);
        dto2.setCourseId(10);
        dto2.setEnrolledAt(Instant.now());
        dto2.setStatus("ACTIVO");

        when(enrollmentService.findByCourseId(10)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/courses/10/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[0].studentId").value(2))
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.enrollmentDTOList[1].studentId").value(3))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("POST /api/courses/{courseId}/enrollments debe crear y asociar una inscripción a un curso")
    void testCreateForCourse() throws Exception {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(3);
        dto.setStudentId(4);
        dto.setCourseId(10);
        dto.setEnrolledAt(Instant.now());
        dto.setStatus("ACTIVO");
        when(enrollmentService.createForCourse(eq(10), any(EnrollmentDTO.class))).thenReturn(dto);

        String json = "{" +
                "\"studentId\":4," +
                "\"courseId\":10," +
                "\"enrolledAt\":\"2024-01-01T00:00:00Z\"," +
                "\"status\":\"ACTIVO\"}";

        mockMvc.perform(post("/api/courses/10/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.studentId").value(4))
                .andExpect(jsonPath("$.courseId").value(10));
    }
} 