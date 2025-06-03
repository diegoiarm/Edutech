package com.edutech.classroom.service;

import com.edutech.classroom.dto.CourseDTO;
import com.edutech.classroom.entity.Course;
import com.edutech.classroom.entity.CourseCategory;
import com.edutech.classroom.entity.User;
import com.edutech.classroom.exception.ResourceNotFoundException;
import com.edutech.classroom.exception.ValidationException;
import com.edutech.classroom.repository.CourseCategoryRepository;
import com.edutech.classroom.repository.CourseRepository;
import com.edutech.classroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseCategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<CourseDTO> findAll() {
        return courseRepository.findAll().stream()
                .map(CourseDTO::fromEntity)
                .toList();
    }

    public CourseDTO findById(Integer id) {
        return CourseDTO.fromEntity(courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado.")));
    }

    public List<CourseDTO> findByCategory(Integer categoryId) {
        return courseRepository.findByCategoryId(categoryId).stream()
                .map(CourseDTO::fromEntity)
                .toList();
    }

    public List<CourseDTO> findByManager(Integer managerId) {
        return courseRepository.findByManagerId(managerId).stream()
                .map(CourseDTO::fromEntity)
                .toList();
    }

    public List<CourseDTO> findByInstructor(Integer instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(CourseDTO::fromEntity)
                .toList();
    }

    public List<CourseDTO> findByStatus(String status) {
        return courseRepository.findByStatus(status).stream()
                .map(CourseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public CourseDTO create(CourseDTO dto) {
        if (courseRepository.existsByTitle(dto.getTitle())) {
            throw new ValidationException("Ya existe un curso con ese título.");
        }

        Course course = dto.toEntity();
        
        // Validar y establecer la categoría
        CourseCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada."));
        course.setCategory(category);

        // Validar y establecer el gestor
        User manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Gestor no encontrado."));
        course.setManager(manager);

        // Validar y establecer el instructor
        User instructor = userRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor no encontrado."));
        course.setInstructor(instructor);

        return CourseDTO.fromEntity(courseRepository.save(course));
    }

    @Transactional
    public CourseDTO update(Integer id, CourseDTO dto) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));

        // Verificar si el título ya existe en otro curso
        if (!existingCourse.getTitle().equals(dto.getTitle()) && 
            courseRepository.existsByTitle(dto.getTitle())) {
            throw new ValidationException("Ya existe un curso con ese título.");
        }

        Course course = dto.toEntity();
        course.setId(id);

        // Validar y establecer la categoría
        CourseCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada."));
        course.setCategory(category);

        // Validar y establecer el gestor
        User manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Gestor no encontrado."));
        course.setManager(manager);

        // Validar y establecer el instructor
        User instructor = userRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor no encontrado."));
        course.setInstructor(instructor);

        return CourseDTO.fromEntity(courseRepository.save(course));
    }

    @Transactional
    public void delete(Integer id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso no encontrado.");
        }
        courseRepository.deleteById(id);
    }

    @Transactional
    public CourseDTO updateStatus(Integer id, String status) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));
        course.setStatus(status);
        return CourseDTO.fromEntity(courseRepository.save(course));
    }
} 