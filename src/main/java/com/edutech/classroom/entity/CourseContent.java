package com.edutech.classroom.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course_content")
public class CourseContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Size(max = 200)
    @NotNull
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size(max = 50)
    @NotNull
    @Column(name = "content_type", nullable = false, length = 50)
    private String contentType;

    @Size(max = 800)
    @NotNull
    @Column(name = "url", nullable = false, length = 800)
    private String url;

    @NotNull
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

}