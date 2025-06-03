package com.edutech.classroom.service;

import com.edutech.classroom.dto.*;
import com.edutech.classroom.entity.*;
import com.edutech.classroom.exception.ResourceNotFoundException;
import com.edutech.classroom.exception.ValidationException;
import com.edutech.classroom.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final CourseQuizRepository quizRepository;
    private final CourseQuizQuestionRepository questionRepository;
    private final QuizResponseRepository responseRepository;
    private final StudentMarkRepository markRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    // Métodos para CourseQuiz
    public List<CourseQuizDTO> findAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(CourseQuizDTO::fromEntity)
                .toList();
    }

    public CourseQuizDTO findQuizById(Integer id) {
        return CourseQuizDTO.fromEntity(quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz no encontrado.")));
    }

    public List<CourseQuizDTO> findQuizzesByCourse(Integer courseId) {
        return quizRepository.findByCourseId(courseId).stream()
                .map(CourseQuizDTO::fromEntity)
                .toList();
    }

    public List<CourseQuizDTO> findQuizzesByType(String quizType) {
        return quizRepository.findByQuizType(quizType).stream()
                .map(CourseQuizDTO::fromEntity)
                .toList();
    }

    @Transactional
    public CourseQuizDTO createQuiz(CourseQuizDTO dto) {
        // Validar que el curso existe
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));

        // Validar que no existe un quiz con el mismo título en el curso
        if (quizRepository.existsByCourseIdAndTitle(dto.getCourseId(), dto.getTitle())) {
            throw new ValidationException("Ya existe un quiz con ese título en este curso.");
        }

        CourseQuiz quiz = dto.toEntity();
        quiz.setCourse(course);

        return CourseQuizDTO.fromEntity(quizRepository.save(quiz));
    }

    @Transactional
    public CourseQuizDTO updateQuiz(Integer id, CourseQuizDTO dto) {
        CourseQuiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz no encontrado."));

        // Validar que el curso existe
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));

        // Validar que no existe otro quiz con el mismo título en el curso
        if (!existingQuiz.getTitle().equals(dto.getTitle()) &&
            quizRepository.existsByCourseIdAndTitle(dto.getCourseId(), dto.getTitle())) {
            throw new ValidationException("Ya existe un quiz con ese título en este curso.");
        }

        CourseQuiz quiz = dto.toEntity();
        quiz.setId(id);
        quiz.setCourse(course);

        return CourseQuizDTO.fromEntity(quizRepository.save(quiz));
    }

    @Transactional
    public void deleteQuiz(Integer id) {
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quiz no encontrado.");
        }
        // Eliminar también las preguntas asociadas
        questionRepository.deleteByQuizId(id);
        quizRepository.deleteById(id);
    }

    // Métodos para CourseQuizQuestion
    public List<CourseQuizQuestionDTO> findQuestionsByQuiz(Integer quizId) {
        return questionRepository.findByQuizIdOrderByOrderIndexAsc(quizId).stream()
                .map(CourseQuizQuestionDTO::fromEntity)
                .toList();
    }

    @Transactional
    public CourseQuizQuestionDTO createQuestion(CourseQuizQuestionDTO dto) {
        // Validar que el quiz existe
        CourseQuiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz no encontrado."));

        CourseQuizQuestion question = dto.toEntity();
        question.setQuiz(quiz);

        return CourseQuizQuestionDTO.fromEntity(questionRepository.save(question));
    }

    @Transactional
    public CourseQuizQuestionDTO updateQuestion(Integer id, CourseQuizQuestionDTO dto) {
        // Validar que el quiz existe
        CourseQuiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz no encontrado."));

        CourseQuizQuestion question = dto.toEntity();
        question.setId(id);
        question.setQuiz(quiz);

        return CourseQuizQuestionDTO.fromEntity(questionRepository.save(question));
    }

    @Transactional
    public void deleteQuestion(Integer id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pregunta no encontrada.");
        }
        questionRepository.deleteById(id);
    }

    // Métodos para QuizResponse
    public List<QuizResponseDTO> findResponsesByQuiz(Integer quizId) {
        return responseRepository.findByQuizId(quizId).stream()
                .map(QuizResponseDTO::fromEntity)
                .toList();
    }

    public List<QuizResponseDTO> findResponsesByStudent(Integer studentId) {
        return responseRepository.findByStudentId(studentId).stream()
                .map(QuizResponseDTO::fromEntity)
                .toList();
    }

    public List<QuizResponseDTO> findResponsesByQuizAndStudent(Integer quizId, Integer studentId) {
        return responseRepository.findByQuizIdAndStudentId(quizId, studentId).stream()
                .map(QuizResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public QuizResponseDTO submitResponse(QuizResponseDTO dto) {
        // Validar que el quiz existe
        CourseQuiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz no encontrado."));

        // Validar que el estudiante existe y está activo
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado."));
        if (!student.getIsActive()) {
            throw new ValidationException("El estudiante no está activo.");
        }

        // Validar que el estudiante está inscrito en el curso
        if (!enrollmentRepository.existsByStudentIdAndCourseId(dto.getStudentId(), quiz.getCourse().getId())) {
            throw new ValidationException("El estudiante no está inscrito en este curso.");
        }

        QuizResponse response = dto.toEntity();
        response.setQuiz(quiz);
        response.setStudent(student);

        return QuizResponseDTO.fromEntity(responseRepository.save(response));
    }

    // Métodos para StudentMark
    public List<StudentMarkDTO> findMarksByQuiz(Integer quizId) {
        return markRepository.findByQuizId(quizId).stream()
                .map(StudentMarkDTO::fromEntity)
                .toList();
    }

    public List<StudentMarkDTO> findMarksByStudent(Integer studentId) {
        return markRepository.findByStudentId(studentId).stream()
                .map(StudentMarkDTO::fromEntity)
                .toList();
    }

    public StudentMarkDTO findMarkByQuizAndStudent(Integer quizId, Integer studentId) {
        return StudentMarkDTO.fromEntity(markRepository.findByQuizIdAndStudentId(quizId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Calificación no encontrada.")));
    }

    @Transactional
    public StudentMarkDTO gradeQuiz(Integer quizId, Integer studentId, StudentMarkDTO dto) {
        // Validar que el quiz existe
        CourseQuiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz no encontrado."));

        // Validar que el estudiante existe y está activo
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado."));
        if (!student.getIsActive()) {
            throw new ValidationException("El estudiante no está activo.");
        }

        // Validar que el estudiante ha enviado una respuesta
        if (!responseRepository.existsByQuizIdAndStudentId(quizId, studentId)) {
            throw new ValidationException("El estudiante no ha enviado una respuesta para este quiz.");
        }

        // Validar que la calificación está en el rango correcto
        if (dto.getMark().compareTo(BigDecimal.ZERO) < 0 || dto.getMark().compareTo(new BigDecimal("100")) > 0) {
            throw new ValidationException("La calificación debe estar entre 0 y 100.");
        }

        StudentMark mark = dto.toEntity();
        mark.setQuiz(quiz);
        mark.setStudent(student);

        return StudentMarkDTO.fromEntity(markRepository.save(mark));
    }

    @Transactional
    public StudentMarkDTO updateMark(Integer id, StudentMarkDTO dto) {
        StudentMark existingMark = markRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calificación no encontrada."));

        // Validar que la calificación está en el rango correcto
        if (dto.getMark().compareTo(BigDecimal.ZERO) < 0 || dto.getMark().compareTo(new BigDecimal("100")) > 0) {
            throw new ValidationException("La calificación debe estar entre 0 y 100.");
        }

        StudentMark mark = dto.toEntity();
        mark.setId(id);
        mark.setQuiz(existingMark.getQuiz());
        mark.setStudent(existingMark.getStudent());

        return StudentMarkDTO.fromEntity(markRepository.save(mark));
    }

    @Transactional
    public void deleteMark(Integer id) {
        if (!markRepository.existsById(id)) {
            throw new ResourceNotFoundException("Calificación no encontrada.");
        }
        markRepository.deleteById(id);
    }

    // Método para calificar automáticamente quizzes de opción múltiple
    @Transactional
    public StudentMarkDTO autoGradeQuiz(Integer quizId, Integer studentId) {
        // Validar que el quiz existe
        CourseQuiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz no encontrado."));

        // Validar que el estudiante existe y está activo
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado."));
        if (!student.getIsActive()) {
            throw new ValidationException("El estudiante no está activo.");
        }

        // Obtener la última respuesta del estudiante
        QuizResponse response = responseRepository.findFirstByQuizIdAndStudentIdOrderBySubmittedAtDesc(quizId, studentId)
                .orElseThrow(() -> new ValidationException("El estudiante no ha enviado una respuesta para este quiz."));

        // Obtener todas las preguntas del quiz
        List<CourseQuizQuestion> questions = questionRepository.findByQuizIdOrderByOrderIndexAsc(quizId);
        if (questions.isEmpty()) {
            throw new ValidationException("El quiz no tiene preguntas.");
        }

        // Calcular la calificación
        int correctAnswers = 0;
        for (CourseQuizQuestion question : questions) {
            if (question.getCorrectOption() != null && 
                question.getCorrectOption().equals(response.getSelectedOption())) {
                correctAnswers++;
            }
        }

        BigDecimal mark = new BigDecimal(correctAnswers)
                .multiply(new BigDecimal("100"))
                .divide(new BigDecimal(questions.size()), 2, RoundingMode.HALF_UP);

        // Crear o actualizar la calificación
        StudentMark studentMark = markRepository.findByQuizIdAndStudentId(quizId, studentId)
                .orElse(new StudentMark());

        studentMark.setQuiz(quiz);
        studentMark.setStudent(student);
        studentMark.setMark(mark);
        studentMark.setComments("Calificación automática basada en respuestas de opción múltiple.");

        return StudentMarkDTO.fromEntity(markRepository.save(studentMark));
    }
} 