package com.edutech.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.courses.entity.CourseCategory;

@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Integer> {

}
