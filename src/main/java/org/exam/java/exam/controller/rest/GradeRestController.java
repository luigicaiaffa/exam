package org.exam.java.exam.controller.rest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.Exam;
import org.exam.java.exam.model.Grade;
import org.exam.java.exam.model.User;
import org.exam.java.exam.service.GradeService;
import org.exam.java.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/public/grades")
@CrossOrigin(origins = "http://localhost:5173")
public class GradeRestController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> index() {

        try {
            Integer guestId = 999;
            Optional<User> user = userService.findById(guestId);

            if (user.isEmpty()) {
                return new ResponseEntity<List<Grade>>(HttpStatus.UNAUTHORIZED);
            }

            Map<String, BigDecimal> averages = gradeService.getAveragesByUserId(guestId);
            List<Grade> grades = gradeService.findAllByUserId(guestId);

            Map<String, Object> response = new HashMap<>();
            response.put("arithmeticAvg", averages.get("arithmetic"));
            response.put("weightedAvg", averages.get("weighted"));
            response.put("totalCfu", averages.get("totalCfu"));
            response.put("maxCfu", user.get().getTotalCfu());
            response.put("grades", grades);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Integer id) {

        try {
            Integer guestId = 999;
            Optional<User> user = userService.findById(guestId);

            if (user.isEmpty()) {
                return new ResponseEntity<Exam>(HttpStatus.UNAUTHORIZED);
            }

            Grade grade = gradeService.getById(id);

            if (grade.getExam().getCourse().getUser().getId().equals(guestId)) {
                return new ResponseEntity<Grade>(grade, HttpStatus.OK);
            } else {
                return new ResponseEntity<Grade>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun voto trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid Grade grade,
            BindingResult bindingResult, Authentication auth) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.toList());
            return new ResponseEntity<List<String>>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<User> user = userService.findByUsername(auth.getName());

            if (user.isEmpty()) {
                return new ResponseEntity<Course>(HttpStatus.UNAUTHORIZED);
            }

            Integer userId = user.get().getId();
            Grade existingGrade = gradeService.getById(id);

            if (existingGrade.getExam().getCourse().getUser().getId().equals(userId)) {

                existingGrade.setExam(existingGrade.getExam());
                existingGrade.setValue(grade.getValue());

                if (existingGrade.getValue() == 30) {
                    existingGrade.setHasHonors(grade.getHasHonors());
                }

                existingGrade.setHasHonors(false);

                return new ResponseEntity<Grade>(gradeService.update(existingGrade), HttpStatus.OK);
            } else {
                return new ResponseEntity<Grade>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun voto trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, Authentication auth) {

        try {
            Optional<User> user = userService.findByUsername(auth.getName());

            if (user.isEmpty()) {
                return new ResponseEntity<Grade>(HttpStatus.UNAUTHORIZED);
            }

            Integer userId = user.get().getId();
            Grade grade = gradeService.getById(id);

            if (grade.getExam().getCourse().getUser().getId().equals(userId)) {
                grade.getExam().setGrade(null);
                gradeService.deleteById(id);
                return new ResponseEntity<Grade>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<Grade>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun voto trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
