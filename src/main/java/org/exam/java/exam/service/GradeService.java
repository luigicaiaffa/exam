package org.exam.java.exam.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
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

    public List<Grade> findAllByUserId(Integer userId) {
        return gradeRepository.findByExamCourseUserIdOrderByExamCourseCourseYear(userId);
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

    public Map<String, BigDecimal> getAveragesByUserId(Integer userId) {
        List<Grade> grades = gradeRepository.findByExamCourseUserId(userId);

        if (grades.isEmpty()) {
            return Map.of("arithmetic", new BigDecimal(0).setScale(2), "weighted", new BigDecimal(0).setScale(2));
        }

        BigDecimal sum = new BigDecimal(0);
        BigDecimal totalVotes = new BigDecimal(0);
        BigDecimal sumCfu = new BigDecimal(0);
        BigDecimal totalCfu = new BigDecimal(0);

        for (Grade grade : grades) {
            BigDecimal vote = BigDecimal.valueOf(grade.getValue());
            sum = sum.add(vote);
            totalVotes = totalVotes.add(BigDecimal.ONE);

            BigDecimal cfu = BigDecimal.valueOf(grade.getExam().getCourse().getCfu());
            sumCfu = sumCfu.add(vote.multiply(cfu));
            totalCfu = totalCfu.add(cfu);
        }

        BigDecimal arithmeticAvg = sum.divide(totalVotes, 2, RoundingMode.HALF_UP);
        BigDecimal weightedAvg = sumCfu.divide(totalCfu, 2, RoundingMode.HALF_UP);

        return Map.of("arithmetic", arithmeticAvg, "weighted", weightedAvg, "totalCfu", totalCfu);
    }
}
