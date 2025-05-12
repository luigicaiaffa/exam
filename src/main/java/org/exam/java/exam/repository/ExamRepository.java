package org.exam.java.exam.repository;

import java.util.List;

import org.exam.java.exam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    
    public List<Exam> findByCourseUserId(Integer id);

    public List<Exam> findByCourseIdOrderByDateDesc(Integer id);

    public List<Exam> findByCourseUserIdAndGradeIsNotNullOrderByCourseCourseYearDesc(Integer id);

    public List<Exam> findByCourseUserIdAndIsCancelledTrueOrderByDateDesc(Integer id);

    public List<Exam> findByCourseUserIdAndGradeIsNullAndIsCancelledFalseOrderByDateAsc(Integer id);
}
