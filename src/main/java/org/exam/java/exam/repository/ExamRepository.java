package org.exam.java.exam.repository;

import java.util.List;

import org.exam.java.exam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    
    public List<Exam> findByGradeIsNotNull();

    public List<Exam> findByIsCancelledTrue();

    public List<Exam> findByGradeIsNullAndIsCancelledFalse();
}
