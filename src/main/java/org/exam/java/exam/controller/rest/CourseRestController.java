package org.exam.java.exam.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.Exam;
import org.exam.java.exam.model.Grade;
import org.exam.java.exam.model.User;
import org.exam.java.exam.service.CourseService;
import org.exam.java.exam.service.ExamService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/public/courses")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseRestController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExamService examService;

    @GetMapping
    public ResponseEntity<?> index(@RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "year", required = false) Integer year) {

        try {
            Integer guestId = 999;
            Optional<User> user = userService.findById(guestId);

            if (user.isEmpty()) {
                return new ResponseEntity<List<Course>>(HttpStatus.UNAUTHORIZED);
            }

            List<Course> courses = new ArrayList<>();

            if (name != null && !name.isEmpty() && year != null && year != 0) {
                courses = courseService.findUserCoursesByYearAndName(guestId, year, name);
            } else if (year != null && year != 0) {
                courses = courseService.findUserCoursesByYear(guestId, year);
            } else if (name != null && !name.isEmpty()) {
                courses = courseService.findUserCoursesByName(guestId, name);
            } else {
                courses = courseService.findUserCoursesSortedByYear(guestId);
            }

            return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Integer id) {

        try {
            Integer guestId = 999;
            Optional<User> user = userService.findById(999);

            if (user.isEmpty()) {
                return new ResponseEntity<Course>(HttpStatus.UNAUTHORIZED);
            }

            Course course = courseService.getById(id);

            if (course.getUser().getId().equals(guestId)) {
                return new ResponseEntity<Course>(course, HttpStatus.OK);
            } else {
                return new ResponseEntity<Course>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun corso trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid Course course, BindingResult bindingResult,
            Authentication auth) {

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
            course.setUser(userService.getById(userId));

            if (course.getUser().getId().equals(userId)) {
                return new ResponseEntity<Course>(courseService.create(course), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<Course>(HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid Course course,
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

            Course existingCourse = courseService.getById(id);

            if (existingCourse.getUser().getId().equals(userId)) {
                existingCourse.setUser(existingCourse.getUser());
                existingCourse.setName(course.getName());
                existingCourse.setCfu(course.getCfu());
                existingCourse.setCourseYear(course.getCourseYear());
                existingCourse.setIsOptional(course.getIsOptional());

                return new ResponseEntity<Course>(courseService.update(existingCourse), HttpStatus.OK);
            } else {
                return new ResponseEntity<Course>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun corso trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, Authentication auth) {

        try {
            Optional<User> user = userService.findByUsername(auth.getName());

            if (user.isEmpty()) {
                return new ResponseEntity<Course>(HttpStatus.UNAUTHORIZED);
            }

            Integer userId = user.get().getId();
            Course course = courseService.getById(id);

            if (course.getUser().getId().equals(userId)) {
                courseService.deleteById(id);
                return new ResponseEntity<Course>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<Course>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun corso trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/exams")
    public ResponseEntity<?> createExam(@PathVariable Integer id, @RequestBody @Valid Exam exam,
            BindingResult bindingResult,
            Authentication auth) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.toList());
            return new ResponseEntity<List<String>>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<User> user = userService.findByUsername(auth.getName());

            if (user.isEmpty()) {
                return new ResponseEntity<Exam>(HttpStatus.UNAUTHORIZED);
            }

            Integer userId = user.get().getId();
            exam.setCourse(courseService.getById(id));

            if (exam.getCourse().getUser().getId().equals(userId)) {

                if (exam.getCourse().getIsPassed()) {
                    return new ResponseEntity<Grade>(HttpStatus.UNAUTHORIZED);
                }

                return new ResponseEntity<Exam>(examService.create(exam), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<Exam>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun corso trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
