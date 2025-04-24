package org.exam.java.exam.service;

import java.util.List;
import java.util.Optional;

import org.exam.java.exam.model.Grade;
import org.exam.java.exam.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public List<Grade> findAll() {
        return gradeRepository.findAll();
    }

    public Optional<Grade> findById(Integer id) {
        return gradeRepository.findById(id);
    }

    public Grade getById(Integer id) {
        Optional<Grade> gradeAttempt = gradeRepository.findById(id);

        if (gradeAttempt.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return gradeAttempt.get();
    }

    public Grade create(Grade grade) {
        return gradeRepository.save(grade);
    }

    public Grade update(Grade grade) {
        return gradeRepository.save(grade);
    }

    public void delete(Grade grade) {
        gradeRepository.delete(grade);
    }

    public void deleteById(Integer id) {
        Grade grade = getById(id);
        gradeRepository.delete(grade);
    }

    public Boolean existsById(Integer id) {
        return gradeRepository.existsById(id);
    }

    public Boolean exists(Grade grade) {
        return gradeRepository.existsById(grade.getId());
    }
}
