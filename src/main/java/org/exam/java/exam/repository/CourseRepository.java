package org.exam.java.exam.repository;

import java.util.List;

import org.exam.java.exam.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    public List<Course> findByUserId(Integer id);

    public List<Course> findByUserIdOrderByCourseYearDesc(Integer id);

    public List<Course> findByUserIdAndCourseYear(Integer id, Integer year);

    public List<Course> findByUserIdAndNameContainingIgnoreCase(Integer id, String name);

    public List<Course> findByUserIdAndCourseYearAndNameContainingIgnoreCase(Integer id, Integer year, String name);

}
