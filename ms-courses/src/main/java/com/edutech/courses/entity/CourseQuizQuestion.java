package com.edutech.courses.entity;

import jakarta.persistence.*;
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

    @JoinColumn(name = "quiz_id")
    private Integer quizId;

    @Column(name = "question_text", length = 800)
    private String questionText;

    @Column(name = "option_a", length = 800)
    private String optionA;

    @Column(name = "option_b", length = 800)
    private String optionB;

    @Column(name = "option_c", length = 800)
    private String optionC;

    @Column(name = "option_d", length = 800)
    private String optionD;

    @Column(name = "option_e", length = 800)
    private String optionE;

    @Column(name = "correct_answer", length = 800)
    private String correctAnswer;

    @Column(name = "correct_option", length = 1)
    private String correctOption;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}