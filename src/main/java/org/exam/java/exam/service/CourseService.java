package org.exam.java.exam.service;

import java.util.List;
import java.util.Optional;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.Exam;
import org.exam.java.exam.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ExamService examService;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public List<Course> findUserCoursesSortedByYear(Integer userId) {
        return courseRepository.findByUserIdOrderByCourseYear(userId);
    }

    public List<Course> findUserCoursesByName(Integer userId, String name) {
        return courseRepository.findByUserIdAndNameContainingIgnoreCase(userId, name);
    }

    public Optional<Course> findById(Integer id) {
        return courseRepository.findById(id);
    }

    public Course getById(Integer id) {
        Optional<Course> courseAttempt = courseRepository.findById(id);

        if (courseAttempt.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return courseAttempt.get();
    }

    public Course create(Course course) {
        return courseRepository.save(course);
    }

    public Course update(Course course) {
        return courseRepository.save(course);
    }

    public void delete(Course course) {
        for (Exam exam : course.getExams()) {
            examService.delete(exam);
        }

        courseRepository.delete(course);
    }

    public void deleteById(Integer id) {
        Course course = getById(id);

        for (Exam exam : course.getExams()) {
            examService.delete(exam);
        }

        courseRepository.delete(course);
    }

    public Boolean existsById(Integer id) {
        return courseRepository.existsById(id);
    }

    public Boolean exists(Course course) {
        return courseRepository.existsById(course.getId());
    }
}
