package com.edutech.courses.service;

import com.edutech.common.dto.CourseCategoryDTO;
import com.edutech.courses.entity.CourseCategory;
import com.edutech.courses.mapper.CourseCategoryMapper;
import com.edutech.courses.repository.CourseCategoryRepository;
import com.edutech.courses.repository.CourseRepository;
import com.edutech.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.edutech.common.exception.ExceptionUtils.orThrow;

@Service
@RequiredArgsConstructor
public class CourseCategoryService {

    private final CourseCategoryRepository categRepo;
    private final CourseCategoryMapper categMapper;
    private final CourseRepository courseRepo;

    public List<CourseCategoryDTO> findAll() {
        return categRepo.findAll().stream().map(categMapper::toDTO).toList();
    }

    public CourseCategoryDTO findById(Integer id) {
        return categMapper.toDTO(orThrow(categRepo.findById(id), "Categoría"));
    }

    public CourseCategoryDTO create(CourseCategoryDTO dto) {
        return saveDTO(dto, null);
    }

    public CourseCategoryDTO update(Integer id, CourseCategoryDTO dto) {
        orThrow(categRepo.findById(id), "Categoría");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        categRepo.delete(orThrow(categRepo.findById(id), "Categoría"));
    }

    private CourseCategoryDTO saveDTO(CourseCategoryDTO dto, Integer id) {
        CourseCategory entity = categMapper.toEntity(dto);
        if (id != null) entity.setId(id);
        return categMapper.toDTO(categRepo.save(entity));
    }

    public CourseCategory save(CourseCategory category) {
        return categRepo.save(category);
    }

    public void deleteById(Integer id) {
        categRepo.deleteById(id);
    }

    // NUEVO: Obtener la categoría de un curso específico
    public CourseCategoryDTO findCategoryByCourseId(Integer courseId) {
        var course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));
        var category = categRepo.findById(course.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        return categMapper.toDTO(category);
    }

    // NUEVO: Crear una categoría y asociarla a un curso
    public CourseCategoryDTO createForCourse(Integer courseId, CourseCategoryDTO dto) {
        var category = categMapper.toEntity(dto);
        var savedCategory = categRepo.save(category);
        var course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));
        course.setCategoryId(savedCategory.getId());
        courseRepo.save(course);
        return categMapper.toDTO(savedCategory);
    }
}
