package com.edutech.academic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.academic.entity.CourseQuiz;

@Repository
public interface CourseQuizRepository extends JpaRepository<CourseQuiz, Integer> {

}
