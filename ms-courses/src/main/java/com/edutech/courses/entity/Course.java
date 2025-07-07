package com.edutech.courses.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false, length = 800)
    private String description;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "manager_id", nullable = false)
    private Integer managerId;

    @Column(name = "instructor_id", nullable = false)
    private Integer instructorId;

    @Column(name = "publish_date", nullable = false)
    private LocalDate publishDate;

    @Column(name = "price", nullable = false, precision = 15, scale = 3)
    private BigDecimal price;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

}