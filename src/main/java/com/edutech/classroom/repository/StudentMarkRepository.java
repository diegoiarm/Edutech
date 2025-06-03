package com.edutech.classroom.repository;

import com.edutech.classroom.entity.StudentMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentMarkRepository extends JpaRepository<StudentMark, Integer> {
    List<StudentMark> findByQuizId(Integer quizId);
    List<StudentMark> findByStudentId(Integer studentId);
    Optional<StudentMark> findByQuizIdAndStudentId(Integer quizId, Integer studentId);
    boolean existsByQuizIdAndStudentId(Integer quizId, Integer studentId);
} 