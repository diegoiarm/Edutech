package com.edutech.classroom.service;

import com.edutech.classroom.dto.CourseCategoryDTO;
import com.edutech.classroom.entity.CourseCategory;
import com.edutech.classroom.exception.ResourceNotFoundException;
import com.edutech.classroom.repository.CourseCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseCategoryService {

    private final CourseCategoryRepository repo;

    public List<CourseCategoryDTO> findAll() {
        return repo.findAll().stream().map(CourseCategoryDTO::fromEntity).toList();
    }

    public CourseCategoryDTO findById(Integer id) {
        return CourseCategoryDTO.fromEntity(repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada.")));
    }

    public CourseCategoryDTO create(CourseCategoryDTO dto) {
        return CourseCategoryDTO.fromEntity(repo.save(dto.toEntity()));
    }

    public CourseCategoryDTO update(Integer id, CourseCategoryDTO dto) {
        repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada."));
        CourseCategory entity = dto.toEntity();
        entity.setId(id);
        return CourseCategoryDTO.fromEntity(repo.save(entity));
    }

    public void delete(Integer id) {
        repo.delete(repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada.")));
    }

}
