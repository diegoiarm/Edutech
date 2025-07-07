package com.edutech.courses.service;

import com.edutech.courses.entity.Enrollment;
import com.edutech.courses.repository.EnrollmentRepository;
import com.edutech.courses.mapper.EnrollmentMapper;
import com.edutech.common.dto.EnrollmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public Optional<Enrollment> findById(Integer id) {
        return enrollmentRepository.findById(id);
    }

    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public void deleteById(Integer id) {
        enrollmentRepository.deleteById(id);
    }

    public List<EnrollmentDTO> findByCourseId(Integer courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
            .map(enrollmentMapper::toDTO)
            .toList();
    }

    public EnrollmentDTO createForCourse(Integer courseId, EnrollmentDTO dto) {
        dto.setCourseId(courseId);
        if (dto.getEnrolledAt() == null) dto.setEnrolledAt(Instant.now());
        Enrollment entity = enrollmentMapper.toEntity(dto);
        Enrollment saved = enrollmentRepository.save(entity);
        return enrollmentMapper.toDTO(saved);
    }
} 