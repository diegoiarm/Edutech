package com.edutech.courses.repository;

import com.edutech.courses.entity.Enrollment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class EnrollmentRepositoryTest {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    @DisplayName("Guardar y buscar una matrícula por ID")
    void testSaveAndFindById() {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(1);
        enrollment.setCourseId(2);
        enrollment.setEnrolledAt(Instant.now());
        enrollment.setStatus("ACTIVO");
        enrollment = enrollmentRepository.save(enrollment);
        Optional<Enrollment> found = enrollmentRepository.findById(enrollment.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo("ACTIVO");
        System.out.println("✅ Test 'Guardar y buscar una matrícula por ID' pasó correctamente.");
    }
} 