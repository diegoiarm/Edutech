package com.edutech.academic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.academic.entity.CourseComment;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, Integer> {

}
