package com.edutech.marks.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "quiz_response")
public class QuizResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "quiz_id", nullable = false)
    private Integer quizId;

    @NotNull
    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Size(max = 1)
    @Column(name = "selected_option", length = 1)
    private String selectedOption;

    @Size(max = 800)
    @Column(name = "response_content", length = 800)
    private String responseContent;

    @Size(max = 800)
    @Column(name = "assignment_url", length = 800)
    private String assignmentUrl;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;
} 