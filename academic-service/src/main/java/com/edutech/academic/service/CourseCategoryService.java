package com.edutech.academic.service;

import com.edutech.common.dto.CourseCategoryDTO;
import com.edutech.academic.entity.CourseCategory;
import com.edutech.academic.mapper.CourseCategoryMapper;
import com.edutech.academic.repository.CourseCategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.edutech.common.exception.ExceptionUtils.orThrow;

@Service
@RequiredArgsConstructor
public class CourseCategoryService {

    private final CourseCategoryRepository categRepo;
    private final CourseCategoryMapper categMapper;

    public List<CourseCategoryDTO> findAll() {
        return categRepo.findAll().stream().map(categMapper::toDTO).toList();
    }

    public CourseCategoryDTO findById(Integer id) {
        return categMapper.toDTO(orThrow(categRepo.findById(id), "Rol"));
    }

    public CourseCategoryDTO create(CourseCategoryDTO dto) {
        return saveDTO(dto, null);
    }

    public CourseCategoryDTO update(Integer id, CourseCategoryDTO dto) {
        orThrow(categRepo.findById(id), "Rol");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        categRepo.delete(orThrow(categRepo.findById(id), "Rol"));
    }

    private CourseCategoryDTO saveDTO(CourseCategoryDTO dto, Integer id) {
        CourseCategory entity = categMapper.toEntity(dto);
        if (id != null) entity.setId(id);
        return categMapper.toDTO(categRepo.save(entity));
    }
}
