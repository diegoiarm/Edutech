package com.edutech.marks.controller;

import com.edutech.common.dto.QuizResponseDTO;
import com.edutech.marks.service.QuizResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-responses")
@RequiredArgsConstructor
public class QuizResponseController {

    private final QuizResponseService quizResponseService;

    @GetMapping
    public ResponseEntity<List<QuizResponseDTO>> getAll() {
        return ResponseEntity.ok(quizResponseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(quizResponseService.findById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<QuizResponseDTO>> getByStudentId(@PathVariable Integer studentId) {
        return ResponseEntity.ok(quizResponseService.findByStudentId(studentId));
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuizResponseDTO>> getByQuizId(@PathVariable Integer quizId) {
        return ResponseEntity.ok(quizResponseService.findByQuizId(quizId));
    }

    @PostMapping
    public ResponseEntity<QuizResponseDTO> create(@RequestBody QuizResponseDTO dto) {
        return ResponseEntity.ok(quizResponseService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> update(@PathVariable Integer id, @RequestBody QuizResponseDTO dto) {
        return ResponseEntity.ok(quizResponseService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        quizResponseService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 