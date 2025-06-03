package com.edutech.classroom.dto;

import com.edutech.classroom.entity.CourseContent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseContentDTO {
    private Integer id;

    @NotNull(message = "El ID del curso es requerido.")
    private Integer courseId;

    @NotNull(message = "El título es requerido.")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres.")
    private String title;

    @NotNull(message = "El tipo de contenido es requerido.")
    @Size(max = 50, message = "El tipo de contenido no puede superar los 50 caracteres.")
    private String contentType;

    @NotNull(message = "La URL es requerida.")
    @Size(max = 800, message = "La URL no puede superar los 800 caracteres.")
    private String url;

    @NotNull(message = "El índice de orden es requerido.")
    private Integer orderIndex;

    public static CourseContentDTO fromEntity(CourseContent entity) {
        CourseContentDTO dto = new CourseContentDTO();
        dto.setId(entity.getId());
        dto.setCourseId(entity.getCourse().getId());
        dto.setTitle(entity.getTitle());
        dto.setContentType(entity.getContentType());
        dto.setUrl(entity.getUrl());
        dto.setOrderIndex(entity.getOrderIndex());
        return dto;
    }

    public CourseContent toEntity() {
        CourseContent entity = new CourseContent();
        entity.setId(this.getId());
        // La relación con Course se establecerá en el servicio
        entity.setTitle(this.getTitle());
        entity.setContentType(this.getContentType());
        entity.setUrl(this.getUrl());
        entity.setOrderIndex(this.getOrderIndex());
        return entity;
    }
} 