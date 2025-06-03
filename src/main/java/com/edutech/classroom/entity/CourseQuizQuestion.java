package com.edutech.classroom.entity;

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
@Table(name = "course_quiz_question")
public class CourseQuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private CourseQuiz quiz;

    @Size(max = 800)
    @Column(name = "question_text", length = 800)
    private String questionText;

    @Size(max = 800)
    @Column(name = "option_a", length = 800)
    private String optionA;

    @Size(max = 800)
    @Column(name = "option_b", length = 800)
    private String optionB;

    @Size(max = 800)
    @Column(name = "option_c", length = 800)
    private String optionC;

    @Size(max = 800)
    @Column(name = "option_d", length = 800)
    private String optionD;

    @Size(max = 800)
    @Column(name = "option_e", length = 800)
    private String optionE;

    @Size(max = 800)
    @Column(name = "correct_answer", length = 800)
    private String correctAnswer;

    @Size(max = 1)
    @Column(name = "correct_option", length = 1)
    private String correctOption;

    @NotNull
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}