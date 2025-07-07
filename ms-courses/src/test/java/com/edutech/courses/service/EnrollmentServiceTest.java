package com.edutech.courses.service;

import com.edutech.courses.entity.Enrollment;
import com.edutech.courses.repository.EnrollmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {
    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    @DisplayName("Debería retornar todas las matrículas")
    void testFindAll() {
        Enrollment e1 = new Enrollment();
        e1.setId(1);
        e1.setStatus("ACTIVO");
        Enrollment e2 = new Enrollment();
        e2.setId(2);
        e2.setStatus("INACTIVO");
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList(e1, e2));
        List<Enrollment> result = enrollmentService.findAll();
        assertThat(result).hasSize(2);
        verify(enrollmentRepository).findAll();
        System.out.println("✅ testFindAll en EnrollmentService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería retornar una matrícula por ID")
    void testFindById() {
        Enrollment e = new Enrollment();
        e.setId(1);
        e.setStatus("ACTIVO");
        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(e));
        Optional<Enrollment> result = enrollmentService.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo("ACTIVO");
        verify(enrollmentRepository).findById(1);
        System.out.println("✅ testFindById en EnrollmentService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería guardar una matrícula")
    void testSave() {
        Enrollment e = new Enrollment();
        e.setStatus("ACTIVO");
        when(enrollmentRepository.save(e)).thenReturn(e);
        Enrollment result = enrollmentService.save(e);
        assertThat(result.getStatus()).isEqualTo("ACTIVO");
        verify(enrollmentRepository).save(e);
        System.out.println("✅ testSave en EnrollmentService pasó correctamente.");
    }

    @Test
    @DisplayName("Debería eliminar una matrícula por ID")
    void testDeleteById() {
        enrollmentService.deleteById(1);
        verify(enrollmentRepository).deleteById(1);
        System.out.println("✅ testDeleteById en EnrollmentService pasó correctamente.");
    }
} 