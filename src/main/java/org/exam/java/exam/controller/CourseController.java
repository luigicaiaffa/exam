package org.exam.java.exam.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.Exam;
import org.exam.java.exam.model.User;
import org.exam.java.exam.service.CourseService;
import org.exam.java.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model, @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "year", required = false) Integer year, Authentication auth) {

        try {
            List<Course> courses = new ArrayList<>();
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            if (name != null && !name.isEmpty() && year != null && year != 0) {
                courses = courseService.findUserCoursesByYearAndName(userId, year, name);
            } else if (year != null && year != 0) {
                courses = courseService.findUserCoursesByYear(userId, year);
            } else if (name != null && !name.isEmpty()) {
                courses = courseService.findUserCoursesByName(userId, name);
            } else {
                courses = courseService.findUserCoursesSortedByYear(userId);
            }

            List<Integer> coursesYears = courseService.findUserCoursesSortedByYear(userId).stream()
                    .map(Course::getCourseYear).distinct().sorted().collect(Collectors.toList());

            model.addAttribute("courses", courses);
            model.addAttribute("coursesYears", coursesYears);

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "/main/error";
        }

        return "/course/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model, Authentication auth) {

        try {
            Course course = courseService.getById(id);
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            if (course.getUser().getId().equals(userId)) {
                model.addAttribute("course", course);
            } else {
                throw new EntityNotFoundException();
            }

        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Course");
            return "/main/notfound";
        }

        return "/course/show";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("course", new Course());
        return "/course/form";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("course") Course formCourse, BindingResult bindingResult, Model model,
            Authentication auth) {

        if (bindingResult.hasErrors()) {
            return "/course/form";
        }

        Optional<User> user = userService.findByUsername(auth.getName());
        Integer userId = user.get().getId();

        formCourse.setUser(userService.getById(userId));
        courseService.create(formCourse);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, Authentication auth) {

        try {
            Course course = courseService.getById(id);
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            if (course.getUser().getId().equals(userId)) {
                model.addAttribute("edit", true);
                model.addAttribute("course", course);
            } else {
                throw new EntityNotFoundException();
            }

        } catch (Exception e) {
            model.addAttribute("element", "Course");
            return "/main/notfound";
        }

        return "/course/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("course") Course formCourse,
            BindingResult bindingResult, Model model, Authentication auth) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            model.addAttribute("exam", formCourse);
            return "/course/form";
        }

        try {
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();
            formCourse.setUser(userService.getById(userId));

            if (formCourse.getUser().getId().equals(userId)) {
                model.addAttribute("edit", true);
                courseService.update(formCourse);
            } else {
                throw new EntityNotFoundException();
            }

        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Course");
            return "/main/notfound";
        }

        return "redirect:/courses/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {

        courseService.deleteById(id);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/exam")
    public String createExam(@PathVariable Integer id, Model model, Authentication auth) {

        try {
            Exam exam = new Exam();
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();
            exam.setCourse(courseService.getById(id));

            if (exam.getCourse().getUser().getId().equals(userId)) {
                model.addAttribute("exam", exam);
            } else {
                throw new EntityNotFoundException();
            }

        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Course");
            return "/main/notfound";
        }

        return "/exam/form";
    }

}
