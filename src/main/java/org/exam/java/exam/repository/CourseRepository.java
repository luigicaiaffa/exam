package org.exam.java.exam.repository;

import java.util.List;

import org.exam.java.exam.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    
    public List<Course> findByNameContaining(String name);
}
