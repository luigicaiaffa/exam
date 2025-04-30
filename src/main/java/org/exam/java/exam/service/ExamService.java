package org.exam.java.exam.service;

import java.util.List;
import java.util.Optional;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.Exam;
import org.exam.java.exam.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    public List<Exam> findAll() {
        return examRepository.findAll();
    }

    public Optional<Exam> findById(Integer id) {
        return examRepository.findById(id);
    }

    public Exam getById(Integer id) {
        Optional<Exam> examAttempt = examRepository.findById(id);

        if (examAttempt.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return examAttempt.get();
    }

    public Exam create(Exam exam) {
        return examRepository.save(exam);
    }

    public Exam update(Exam exam) {
        return examRepository.save(exam);
    }

    public void delete(Exam exam) {
        if (exam.getGrade() != null) {
            exam.setGrade(null);
        }

        examRepository.delete(exam);
    }

    public void deleteById(Integer id) {
        Exam exam = getById(id);

        if (exam.getGrade() != null) {
            exam.setGrade(null);
        }

        Course course = exam.getCourse();
        course.setIsPassed(false);

        examRepository.delete(exam);
    }

    public Boolean existsById(Integer id) {
        return examRepository.existsById(id);
    }

    public Boolean exists(Exam exam) {
        return examRepository.existsById(exam.getId());
    }
}
