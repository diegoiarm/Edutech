package com.edutech.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.courses.entity.CourseQuiz;

@Repository
public interface CourseQuizRepository extends JpaRepository<CourseQuiz, Integer> {

}
