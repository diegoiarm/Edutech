package com.edutech.marks.client;

import com.edutech.common.dto.CourseQuizDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "academic-service", path = "/api/quizzes")
public interface CourseQuizClient {
    @GetMapping("/{id}")
    CourseQuizDTO findById(@PathVariable Integer id);
} 