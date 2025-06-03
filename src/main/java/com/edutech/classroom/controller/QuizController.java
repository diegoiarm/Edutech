package com.edutech.classroom.controller;

import com.edutech.classroom.dto.*;
import com.edutech.classroom.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService service;

    // Endpoints para CourseQuiz
    @GetMapping
    public ResponseEntity<List<CourseQuizDTO>> findAllQuizzes() {
        return ResponseEntity.ok(service.findAllQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseQuizDTO> findQuizById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findQuizById(id));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseQuizDTO>> findQuizzesByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(service.findQuizzesByCourse(courseId));
    }

    @GetMapping("/type/{quizType}")
    public ResponseEntity<List<CourseQuizDTO>> findQuizzesByType(@PathVariable String quizType) {
        return ResponseEntity.ok(service.findQuizzesByType(quizType));
    }

    @PostMapping
    public ResponseEntity<CourseQuizDTO> createQuiz(@RequestBody @Valid CourseQuizDTO dto) {
        return ResponseEntity.ok(service.createQuiz(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseQuizDTO> updateQuiz(@PathVariable Integer id, @RequestBody @Valid CourseQuizDTO dto) {
        return ResponseEntity.ok(service.updateQuiz(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Integer id) {
        service.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints para CourseQuizQuestion
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<CourseQuizQuestionDTO>> findQuestionsByQuiz(@PathVariable Integer quizId) {
        return ResponseEntity.ok(service.findQuestionsByQuiz(quizId));
    }

    @PostMapping("/questions")
    public ResponseEntity<CourseQuizQuestionDTO> createQuestion(@RequestBody @Valid CourseQuizQuestionDTO dto) {
        return ResponseEntity.ok(service.createQuestion(dto));
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<CourseQuizQuestionDTO> updateQuestion(@PathVariable Integer id, @RequestBody @Valid CourseQuizQuestionDTO dto) {
        return ResponseEntity.ok(service.updateQuestion(id, dto));
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer id) {
        service.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints para QuizResponse
    @GetMapping("/{quizId}/responses")
    public ResponseEntity<List<QuizResponseDTO>> findResponsesByQuiz(@PathVariable Integer quizId) {
        return ResponseEntity.ok(service.findResponsesByQuiz(quizId));
    }

    @GetMapping("/student/{studentId}/responses")
    public ResponseEntity<List<QuizResponseDTO>> findResponsesByStudent(@PathVariable Integer studentId) {
        return ResponseEntity.ok(service.findResponsesByStudent(studentId));
    }

    @GetMapping("/{quizId}/student/{studentId}/responses")
    public ResponseEntity<List<QuizResponseDTO>> findResponsesByQuizAndStudent(
            @PathVariable Integer quizId,
            @PathVariable Integer studentId) {
        return ResponseEntity.ok(service.findResponsesByQuizAndStudent(quizId, studentId));
    }

    @PostMapping("/responses")
    public ResponseEntity<QuizResponseDTO> submitResponse(@RequestBody @Valid QuizResponseDTO dto) {
        return ResponseEntity.ok(service.submitResponse(dto));
    }

    // Endpoints para StudentMark
    @GetMapping("/{quizId}/marks")
    public ResponseEntity<List<StudentMarkDTO>> findMarksByQuiz(@PathVariable Integer quizId) {
        return ResponseEntity.ok(service.findMarksByQuiz(quizId));
    }

    @GetMapping("/student/{studentId}/marks")
    public ResponseEntity<List<StudentMarkDTO>> findMarksByStudent(@PathVariable Integer studentId) {
        return ResponseEntity.ok(service.findMarksByStudent(studentId));
    }

    @GetMapping("/{quizId}/student/{studentId}/mark")
    public ResponseEntity<StudentMarkDTO> findMarkByQuizAndStudent(
            @PathVariable Integer quizId,
            @PathVariable Integer studentId) {
        return ResponseEntity.ok(service.findMarkByQuizAndStudent(quizId, studentId));
    }

    @PostMapping("/{quizId}/student/{studentId}/grade")
    public ResponseEntity<StudentMarkDTO> gradeQuiz(
            @PathVariable Integer quizId,
            @PathVariable Integer studentId,
            @RequestBody @Valid StudentMarkDTO dto) {
        return ResponseEntity.ok(service.gradeQuiz(quizId, studentId, dto));
    }

    @PutMapping("/marks/{id}")
    public ResponseEntity<StudentMarkDTO> updateMark(@PathVariable Integer id, @RequestBody @Valid StudentMarkDTO dto) {
        return ResponseEntity.ok(service.updateMark(id, dto));
    }

    @DeleteMapping("/marks/{id}")
    public ResponseEntity<Void> deleteMark(@PathVariable Integer id) {
        service.deleteMark(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para calificación automática
    @PostMapping("/{quizId}/student/{studentId}/auto-grade")
    public ResponseEntity<StudentMarkDTO> autoGradeQuiz(
            @PathVariable Integer quizId,
            @PathVariable Integer studentId) {
        return ResponseEntity.ok(service.autoGradeQuiz(quizId, studentId));
    }
} 