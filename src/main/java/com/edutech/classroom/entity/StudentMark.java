package com.edutech.classroom.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "student_mark")
public class StudentMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private CourseQuiz quiz;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @NotNull
    @Column(name = "mark", nullable = false, precision = 5, scale = 2)
    private BigDecimal mark;

    @Size(max = 800)
    @Column(name = "comments", length = 800)
    private String comments;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "graded_at", nullable = false)
    private Instant gradedAt;

}