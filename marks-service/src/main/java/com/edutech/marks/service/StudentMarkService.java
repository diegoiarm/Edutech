package com.edutech.marks.service;

import com.edutech.common.dto.StudentMarkDTO;
import com.edutech.marks.entity.StudentMark;
import com.edutech.marks.mapper.StudentMarkMapper;
import com.edutech.marks.repository.StudentMarkRepository;
import com.edutech.marks.client.CourseQuizClient;
import com.edutech.marks.client.UserClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.edutech.common.exception.ExceptionUtils.orThrow;
import static com.edutech.common.exception.ExceptionUtils.orThrowFeign;

@Service
@RequiredArgsConstructor
public class StudentMarkService {

    private final StudentMarkRepository markRepo;
    private final StudentMarkMapper markMapper;
    private final CourseQuizClient quizClient;
    private final UserClient userClient;

    public List<StudentMarkDTO> findAll() {
        return markRepo.findAll().stream().map(markMapper::toDTO).toList();
    }

    public StudentMarkDTO findById(Integer id) {
        return markMapper.toDTO(orThrow(markRepo.findById(id), "Calificación"));
    }

    public List<StudentMarkDTO> findByStudentId(Integer studentId) {
        return markRepo.findByStudentId(studentId).stream()
            .map(markMapper::toDTO)
            .collect(Collectors.toList());
    }

    public StudentMarkDTO create(StudentMarkDTO dto) {
        // Validar que el quiz existe
        orThrowFeign(dto.getQuizId(), quizClient::findById, "Quiz");

        // Validar que el estudiante existe
        orThrowFeign(dto.getStudentId(), userClient::findById, "Estudiante");

        return saveDTO(dto, null);
    }

    public StudentMarkDTO update(Integer id, StudentMarkDTO dto) {
        orThrow(markRepo.findById(id), "Calificación");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        markRepo.delete(orThrow(markRepo.findById(id), "Calificación"));
    }

    private StudentMarkDTO saveDTO(StudentMarkDTO dto, Integer id) {
        StudentMark entity = markMapper.toEntity(dto);
        if (id != null) {
            entity.setId(id);
        }
        return markMapper.toDTO(markRepo.save(entity));
    }
} 