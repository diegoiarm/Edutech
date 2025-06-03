package com.edutech.classroom.dto;

import com.edutech.classroom.entity.Enrollment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class EnrollmentDTO {
    private Integer id;

    @NotNull(message = "El estudiante es requerido.")
    private Integer studentId;

    @NotNull(message = "El curso es requerido.")
    private Integer courseId;

    private Instant enrolledAt;

    @NotNull(message = "El estado es requerido.")
    @Size(max = 20, message = "El estado no puede superar los 20 caracteres.")
    private String status;

    public static EnrollmentDTO fromEntity(Enrollment entity) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudent().getId());
        dto.setCourseId(entity.getCourse().getId());
        dto.setEnrolledAt(entity.getEnrolledAt());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public Enrollment toEntity() {
        Enrollment entity = new Enrollment();
        entity.setId(this.getId());
        // Las relaciones (student, course) se establecerán en el servicio
        entity.setEnrolledAt(this.getEnrolledAt() != null ? this.getEnrolledAt() : Instant.now());
        entity.setStatus(this.getStatus());
        return entity;
    }
}
