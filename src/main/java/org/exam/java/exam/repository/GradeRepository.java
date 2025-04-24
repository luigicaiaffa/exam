package org.exam.java.exam.repository;

import org.exam.java.exam.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    
}
