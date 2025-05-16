package org.exam.java.exam.repository;

import java.util.List;

import org.exam.java.exam.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    public List<Course> findByUserId(Integer id);

    public List<Course> findByUserIdOrderByIsPassedAscCourseYearDesc(Integer id);

    public List<Course> findByUserIdAndCourseYearOrderByIsPassedAscCourseYearDesc(Integer id, Integer year);

    public List<Course> findByUserIdAndNameContainingIgnoreCaseOrderByIsPassedAscCourseYearDesc(Integer id,
            String name);

    public List<Course> findByUserIdAndCourseYearAndNameContainingIgnoreCaseOrderByIsPassedAscCourseYearDesc(Integer id,
            Integer year,
            String name);

}
