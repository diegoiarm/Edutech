package com.edutech.academic.service;

import com.edutech.common.dto.CourseDTO;
import com.edutech.academic.client.UserClient;
import com.edutech.academic.entity.Course;
import com.edutech.academic.mapper.CourseMapper;
import com.edutech.academic.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.edutech.common.exception.ExceptionUtils.orThrow;
import static com.edutech.common.exception.ExceptionUtils.orThrowFeign;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepo;
    private final CourseMapper courseMapper;
    private final UserClient userClient;

    public List<CourseDTO> findAll() {
        return courseRepo.findAll().stream().map(courseMapper::toDTO).toList();
    }

    public CourseDTO findById(Integer id) {
        return courseMapper.toDTO(orThrow(courseRepo.findById(id), "Rol"));
    }

    /*
     * CREAR UN NUEVO CURSO:
     * AHORA SE USARÁ LA COMUNICACIÓN CON EL MICROSERVICIO DE IDENTIDAD PARA VALIDAR
     * SI EL COORDINADOR E INSTRUCTOR DEL CURSO QUE SE VA A CREAR, REALMENTE
     * EXISTEN EN LA BASE DE DATOS, Y EN CASO CONTRARIO ARROJAR LOS MENSAJES
     * DE ERROR ADECUADOS.
     */

    public CourseDTO create(CourseDTO dto) {

        // Nos comunicaremos con el microservicio de Identidad para validar que el coordinador exista
        orThrowFeign(dto.getManagerId(), userClient::findById, "Coordinador");

        // Nos comunicaremos con el microservicio de Identidad para validar que el instructor exista
        orThrowFeign(dto.getInstructorId(), userClient::findById, "Instructor");

        // Crear nuevo curso
        return saveDTO(dto, null);
    }

    public CourseDTO update(Integer id, CourseDTO dto) {
        orThrow(courseRepo.findById(id), "Rol");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        courseRepo.delete(orThrow(courseRepo.findById(id), "Rol"));
    }

    private CourseDTO saveDTO(CourseDTO dto, Integer id) {
        Course entity = courseMapper.toEntity(dto);
        if (id != null) entity.setId(id);
        return courseMapper.toDTO(courseRepo.save(entity));
    }
}
