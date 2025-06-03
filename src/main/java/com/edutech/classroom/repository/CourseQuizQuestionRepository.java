package com.edutech.classroom.repository;

import com.edutech.classroom.entity.CourseQuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseQuizQuestionRepository extends JpaRepository<CourseQuizQuestion, Integer> {
    List<CourseQuizQuestion> findByQuizId(Integer quizId);
    List<CourseQuizQuestion> findByQuizIdOrderByOrderIndexAsc(Integer quizId);
    void deleteByQuizId(Integer quizId);
} 