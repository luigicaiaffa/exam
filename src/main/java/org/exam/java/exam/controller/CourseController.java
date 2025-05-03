package org.exam.java.exam.controller;

import java.util.ArrayList;
import java.util.List;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.Exam;
import org.exam.java.exam.service.CourseService;
import org.exam.java.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam(name = "year", required = false) Integer year) {

        List<Course> courses = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            courses = courseService.findByName(name);
        } else {
            courses = courseService.findAllSortedByYear();
        }

        model.addAttribute("courses", courses);
        return "/course/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {

        try {
            model.addAttribute("course", courseService.getById(id));
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
    public String store(@Valid @ModelAttribute("course") Course formCourse, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/course/form";
        }

        formCourse.setUser(userService.getById(1));
        courseService.create(formCourse);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {

        try {
            model.addAttribute("edit", true);
            model.addAttribute("course", courseService.getById(id));

        } catch (Exception e) {
            model.addAttribute("element", "Course");
            return "/main/notfound";
        }

        return "/course/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("course") Course formCourse,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/course/form";
        }

        formCourse.setUser(userService.getById(1));
        courseService.update(formCourse);
        return "redirect:/courses/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {

        courseService.deleteById(id);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/exam")
    public String createExam(@PathVariable Integer id, Model model) {

        try {
            Exam exam = new Exam();
            exam.setCourse(courseService.getById(id));
            model.addAttribute("exam", exam);
        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Course");
            return "/main/notfound";
        }

        return "/exam/form";
    }

}
