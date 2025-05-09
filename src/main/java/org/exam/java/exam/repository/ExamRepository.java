package org.exam.java.exam.repository;

import java.util.List;

import org.exam.java.exam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    
    public List<Exam> findByCourseUserId(Integer id);

    public List<Exam> findByCourseUserIdAndGradeIsNotNullOrderByDateDesc(Integer id);

    public List<Exam> findByCourseUserIdAndIsCancelledTrueOrderByDateDesc(Integer id);

    public List<Exam> findByCourseUserIdAndGradeIsNullAndIsCancelledFalseOrderByDateDesc(Integer id);
}
