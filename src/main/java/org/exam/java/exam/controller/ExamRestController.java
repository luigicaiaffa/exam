package org.exam.java.exam.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.Exam;
import org.exam.java.exam.model.User;
import org.exam.java.exam.service.ExamService;
import org.exam.java.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
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
@RequestMapping("/api/exams")
public class ExamRestController {

    @Autowired
    private ExamService examService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> index(Authentication auth) {

        try {
            Optional<User> user = userService.findByUsername(auth.getName());

            if (user.isEmpty()) {
                return new ResponseEntity<List<Exam>>(HttpStatus.UNAUTHORIZED);
            }

            Integer userId = user.get().getId();
            List<Exam> exams = examService.findAllByUserId(userId);

            return new ResponseEntity<List<Exam>>(exams, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Integer id, Authentication auth) {

        try {
            Optional<User> user = userService.findByUsername(auth.getName());

            if (user.isEmpty()) {
                return new ResponseEntity<Exam>(HttpStatus.UNAUTHORIZED);
            }

            Exam exam = examService.getById(id);
            Integer userId = user.get().getId();

            if (exam.getCourse().getUser().getId().equals(userId)) {
                return new ResponseEntity<Exam>(exam, HttpStatus.OK);
            } else {
                return new ResponseEntity<Exam>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun esame trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid Exam exam,
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

            Exam existingExam = examService.getById(id);

            if (existingExam.getCourse().getUser().getId().equals(userId)) {
                existingExam.setCourse(existingExam.getCourse());
                existingExam.setDate(exam.getDate());
                existingExam.setLocation(exam.getLocation());
                existingExam.setIsCancelled(exam.getIsCancelled());
                existingExam.setNotes(exam.getNotes());

                return new ResponseEntity<Exam>(examService.update(existingExam), HttpStatus.OK);
            } else {
                return new ResponseEntity<Exam>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun esame trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, Authentication auth) {

        try {
            Optional<User> user = userService.findByUsername(auth.getName());

            if (user.isEmpty()) {
                return new ResponseEntity<Exam>(HttpStatus.UNAUTHORIZED);
            }

            Integer userId = user.get().getId();
            Exam exam = examService.getById(id);

            if (exam.getCourse().getUser().getId().equals(userId)) {
                examService.deleteById(id);
                return new ResponseEntity<Exam>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<Exam>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun esame trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
