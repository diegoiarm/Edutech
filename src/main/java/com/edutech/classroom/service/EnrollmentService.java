package com.edutech.classroom.service;

import com.edutech.classroom.dto.EnrollmentDTO;
import com.edutech.classroom.entity.Course;
import com.edutech.classroom.entity.Enrollment;
import com.edutech.classroom.entity.User;
import com.edutech.classroom.exception.ResourceNotFoundException;
import com.edutech.classroom.exception.ValidationException;
import com.edutech.classroom.repository.CourseRepository;
import com.edutech.classroom.repository.EnrollmentRepository;
import com.edutech.classroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public List<EnrollmentDTO> findAll() {
        return enrollmentRepository.findAll().stream()
                .map(EnrollmentDTO::fromEntity)
                .toList();
    }

    public EnrollmentDTO findById(Integer id) {
        return EnrollmentDTO.fromEntity(enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada.")));
    }

    public List<EnrollmentDTO> findByStudent(Integer studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(EnrollmentDTO::fromEntity)
                .toList();
    }

    public List<EnrollmentDTO> findByCourse(Integer courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(EnrollmentDTO::fromEntity)
                .toList();
    }

    public List<EnrollmentDTO> findByStatus(String status) {
        return enrollmentRepository.findByStatus(status).stream()
                .map(EnrollmentDTO::fromEntity)
                .toList();
    }

    public long countByCourse(Integer courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }

    @Transactional
    public EnrollmentDTO create(EnrollmentDTO dto) {
        // Verificar si el estudiante ya está inscrito en el curso
        if (enrollmentRepository.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new ValidationException("El estudiante ya está inscrito en este curso.");
        }

        // Validar que el estudiante existe y está activo
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado."));
        if (!student.getIsActive()) {
            throw new ValidationException("El estudiante no está activo.");
        }

        // Validar que el curso existe y está activo
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));
        if (!"ACTIVE".equals(course.getStatus())) {
            throw new ValidationException("El curso no está activo.");
        }

        Enrollment enrollment = dto.toEntity();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return EnrollmentDTO.fromEntity(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public EnrollmentDTO update(Integer id, EnrollmentDTO dto) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada."));

        // Validar que el estudiante existe y está activo
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado."));
        if (!student.getIsActive()) {
            throw new ValidationException("El estudiante no está activo.");
        }

        // Validar que el curso existe y está activo
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));
        if (!"ACTIVE".equals(course.getStatus())) {
            throw new ValidationException("El curso no está activo.");
        }

        // Verificar si el estudiante ya está inscrito en otro curso
        if (!existingEnrollment.getStudent().getId().equals(dto.getStudentId()) ||
            !existingEnrollment.getCourse().getId().equals(dto.getCourseId())) {
            if (enrollmentRepository.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())) {
                throw new ValidationException("El estudiante ya está inscrito en este curso.");
            }
        }

        Enrollment enrollment = dto.toEntity();
        enrollment.setId(id);
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return EnrollmentDTO.fromEntity(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public void delete(Integer id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inscripción no encontrada.");
        }
        enrollmentRepository.deleteById(id);
    }

    @Transactional
    public EnrollmentDTO updateStatus(Integer id, String status) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada."));
        enrollment.setStatus(status);
        return EnrollmentDTO.fromEntity(enrollmentRepository.save(enrollment));
    }
} 