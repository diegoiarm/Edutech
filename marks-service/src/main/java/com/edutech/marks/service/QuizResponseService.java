package com.edutech.marks.service;

import com.edutech.common.dto.QuizResponseDTO;
import com.edutech.marks.entity.QuizResponse;
import com.edutech.marks.mapper.QuizResponseMapper;
import com.edutech.marks.repository.QuizResponseRepository;
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
public class QuizResponseService {

    private final QuizResponseRepository responseRepo;
    private final QuizResponseMapper responseMapper;
    private final CourseQuizClient quizClient;
    private final UserClient userClient;

    public List<QuizResponseDTO> findAll() {
        return responseRepo.findAll().stream().map(responseMapper::toDTO).toList();
    }

    public QuizResponseDTO findById(Integer id) {
        return responseMapper.toDTO(orThrow(responseRepo.findById(id), "Respuesta"));
    }

    public List<QuizResponseDTO> findByStudentId(Integer studentId) {
        return responseRepo.findByStudentId(studentId).stream()
            .map(responseMapper::toDTO)
            .collect(Collectors.toList());
    }

    public List<QuizResponseDTO> findByQuizId(Integer quizId) {
        return responseRepo.findByQuizId(quizId).stream()
            .map(responseMapper::toDTO)
            .collect(Collectors.toList());
    }

    public QuizResponseDTO create(QuizResponseDTO dto) {
        // Validar que el quiz existe
        orThrowFeign(dto.getQuizId(), quizClient::findById, "Quiz");

        // Validar que el estudiante existe
        orThrowFeign(dto.getStudentId(), userClient::findById, "Estudiante");

        return saveDTO(dto, null);
    }

    public QuizResponseDTO update(Integer id, QuizResponseDTO dto) {
        orThrow(responseRepo.findById(id), "Respuesta");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        responseRepo.delete(orThrow(responseRepo.findById(id), "Respuesta"));
    }

    private QuizResponseDTO saveDTO(QuizResponseDTO dto, Integer id) {
        QuizResponse entity = responseMapper.toEntity(dto);
        if (id != null) {
            entity.setId(id);
        }
        return responseMapper.toDTO(responseRepo.save(entity));
    }
} 