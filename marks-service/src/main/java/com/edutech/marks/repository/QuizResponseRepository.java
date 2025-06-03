package com.edutech.marks.repository;

import com.edutech.marks.entity.QuizResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizResponseRepository extends JpaRepository<QuizResponse, Integer> {
    List<QuizResponse> findByQuizId(Integer quizId);
    List<QuizResponse> findByStudentId(Integer studentId);
    List<QuizResponse> findByQuizIdAndStudentId(Integer quizId, Integer studentId);
    Optional<QuizResponse> findFirstByQuizIdAndStudentIdOrderBySubmittedAtDesc(Integer quizId, Integer studentId);
    boolean existsByQuizIdAndStudentId(Integer quizId, Integer studentId);
} 