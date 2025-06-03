package com.edutech.classroom.repository;

import com.edutech.classroom.entity.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Integer> {
    List<CourseContent> findByCourseId(Integer courseId);
    List<CourseContent> findByCourseIdOrderByOrderIndexAsc(Integer courseId);
    List<CourseContent> findByCourseIdAndContentType(Integer courseId, String contentType);
    boolean existsByCourseIdAndTitle(Integer courseId, String title);
} 