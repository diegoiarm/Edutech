package com.edutech.courses.service;

import com.edutech.courses.entity.CourseQuiz;
import com.edutech.courses.repository.CourseQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseQuizService {
    private final CourseQuizRepository quizRepository;

    public List<CourseQuiz> findAll() {
        return quizRepository.findAll();
    }

    public Optional<CourseQuiz> findById(Integer id) {
        return quizRepository.findById(id);
    }

    public CourseQuiz save(CourseQuiz quiz) {
        return quizRepository.save(quiz);
    }

    public void deleteById(Integer id) {
        quizRepository.deleteById(id);
    }
} 