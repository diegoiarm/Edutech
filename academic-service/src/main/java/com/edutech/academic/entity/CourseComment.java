package com.edutech.academic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "course_comment")
public class CourseComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JoinColumn(name = "course_id", nullable = false)
    private Integer courseId;

    @JoinColumn(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "comment_text", nullable = false, length = 800)
    private String commentText;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}