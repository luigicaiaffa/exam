package org.exam.java.exam.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.Exam;
import org.exam.java.exam.model.Grade;
import org.exam.java.exam.model.User;
import org.exam.java.exam.service.CourseService;
import org.exam.java.exam.service.ExamService;
import org.exam.java.exam.service.GradeService;
import org.exam.java.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ExamService examService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model, Authentication auth) {

        try {
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            Map<String, BigDecimal> averages = gradeService.getAveragesByUserId(userId);
            model.addAttribute("arithmeticAvg", averages.get("arithmetic"));
            model.addAttribute("weightedAvg", averages.get("weighted"));
            model.addAttribute("totalCfu", averages.get("totalCfu"));
            model.addAttribute("grades", gradeService.findAllByUserId(userId));
            model.addAttribute("user", user.get());

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "/main/error";
        }

        model.addAttribute("grades", gradeService.findAll());
        return "/grade/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model, Authentication auth) {

        try {
            Grade grade = gradeService.getById(id);
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            if (grade.getExam().getCourse().getUser().getId().equals(userId)) {
                model.addAttribute("grade", grade);
            } else {
                throw new EntityNotFoundException();
            }

        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Grade");
            return "/main/notfound";
        }

        return "/grade/show";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("grade") Grade formGrade, BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "/grade/form";
        }

        try {

            if (formGrade.getValue() != 30) {
                formGrade.setHasHonors(false);
            }

            gradeService.create(formGrade);
            Exam exam = examService.findById(formGrade.getExam().getId())
                    .orElseThrow(() -> new EntityNotFoundException());
            Course course = exam.getCourse();
            course.setIsPassed(true);
            courseService.update(course);

        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Grade");
            return "/main/notfound";
        }

        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, Authentication auth) {

        try {
            Grade grade = gradeService.getById(id);
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            if (grade.getExam().getCourse().getUser().getId().equals(userId)) {
                model.addAttribute("edit", true);
                model.addAttribute("grade", gradeService.getById(id));
            } else {
                throw new EntityNotFoundException();
            }

        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Grade");
            return "/main/notfound";
        }

        return "/grade/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("grade") Grade formGrade,
            BindingResult bindingResult, Model model, Authentication auth) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            model.addAttribute("grade", formGrade);
            return "/grade/form";
        }

        try {
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            if (formGrade.getExam().getCourse().getUser().getId().equals(userId)) {
                gradeService.update(formGrade);
            } else {
                throw new EntityNotFoundException();
            }

        } catch (Exception e) {
            model.addAttribute("element", "Grade");
            return "/main/notfound";
        }

        return "redirect:/courses";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        gradeService.deleteById(id);
        return "redirect:/courses";
    }

}
