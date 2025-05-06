package org.exam.java.exam.repository;

import java.util.List;

import org.exam.java.exam.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Integer> {

    public List<Grade> findByExamCourseUserId(Integer id);

    public List<Grade> findByExamCourseUserIdOrderByExamCourseCourseYear(Integer id);

    public List<Grade> findByExamCourseUserIdAndExamCourseCourseYear(Integer id, Integer year);
}
