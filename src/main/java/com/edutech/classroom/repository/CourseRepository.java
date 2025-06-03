package com.edutech.classroom.repository;

import com.edutech.classroom.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByCategoryId(Integer categoryId);
    List<Course> findByManagerId(Integer managerId);
    List<Course> findByInstructorId(Integer instructorId);
    List<Course> findByStatus(String status);
    boolean existsByTitle(String title);
} 