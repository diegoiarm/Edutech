package com.edutech.classroom.dto;

import com.edutech.classroom.entity.CourseCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseCategoryDTO {

    private Integer id;

    @NotNull(message = "El nombre de la categoría no puede estar vacío.")
    @Size(max = 100, message = "El nombre de la categoría no puede superar los 100 carácteres.")
    private String name;

    @NotNull(message = "La descripción de la categoría no puede estar vacío.")
    @Size(max = 800, message = "El descripción de la categoría no puede superar los 800 carácteres.")
    private String description;

    public static CourseCategoryDTO fromEntity(CourseCategory entity) {
        CourseCategoryDTO dto = new CourseCategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    // De DTO a JPA
    public CourseCategory toEntity() {
        CourseCategory entity = new CourseCategory();
        entity.setId(this.getId());
        entity.setName(this.getName());
        entity.setDescription(this.getDescription());
        return entity;

    }


}
