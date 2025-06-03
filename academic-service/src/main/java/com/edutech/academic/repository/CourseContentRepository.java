package com.edutech.academic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.academic.entity.CourseContent;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Integer> {

}
