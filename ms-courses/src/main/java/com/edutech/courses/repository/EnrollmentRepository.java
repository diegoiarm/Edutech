package com.edutech.courses.repository;

import com.edutech.courses.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByCourseId(Integer courseId);
} 