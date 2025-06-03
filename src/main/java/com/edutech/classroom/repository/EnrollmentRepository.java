package com.edutech.classroom.repository;

import com.edutech.classroom.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByStudentId(Integer studentId);
    List<Enrollment> findByCourseId(Integer courseId);
    List<Enrollment> findByStatus(String status);
    boolean existsByStudentIdAndCourseId(Integer studentId, Integer courseId);
    long countByCourseId(Integer courseId);
} 