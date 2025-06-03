package com.edutech.classroom.dto;

import com.edutech.classroom.entity.Course;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CourseDTO {
    private Integer id;

    @NotNull(message = "El título no puede estar vacío.")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres.")
    private String title;

    @NotNull(message = "La descripción no puede estar vacía.")
    @Size(max = 800, message = "La descripción no puede superar los 800 caracteres.")
    private String description;

    @NotNull(message = "La categoría es requerida.")
    private Integer categoryId;

    @NotNull(message = "El gestor es requerido.")
    private Integer managerId;

    @NotNull(message = "El instructor es requerido.")
    private Integer instructorId;

    @NotNull(message = "La fecha de publicación es requerida.")
    @FutureOrPresent(message = "La fecha de publicación debe ser presente o futura.")
    private LocalDate publishDate;

    @NotNull(message = "El precio es requerido.")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0.")
    private BigDecimal price;

    @NotNull(message = "La imagen es requerida.")
    @Size(max = 255, message = "La URL de la imagen no puede superar los 255 caracteres.")
    private String image;

    @NotNull(message = "El estado es requerido.")
    @Size(max = 50, message = "El estado no puede superar los 50 caracteres.")
    private String status;

    public static CourseDTO fromEntity(Course entity) {
        CourseDTO dto = new CourseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setManagerId(entity.getManager().getId());
        dto.setInstructorId(entity.getInstructor().getId());
        dto.setPublishDate(entity.getPublishDate());
        dto.setPrice(entity.getPrice());
        dto.setImage(entity.getImage());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public Course toEntity() {
        Course entity = new Course();
        entity.setId(this.getId());
        entity.setTitle(this.getTitle());
        entity.setDescription(this.getDescription());
        // Las relaciones (category, manager, instructor) se establecerán en el servicio
        entity.setPublishDate(this.getPublishDate());
        entity.setPrice(this.getPrice());
        entity.setImage(this.getImage());
        entity.setStatus(this.getStatus());
        return entity;
    }
} 