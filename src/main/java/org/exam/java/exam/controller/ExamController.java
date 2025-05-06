package org.exam.java.exam.controller;

import java.util.Optional;

import org.exam.java.exam.model.Exam;
import org.exam.java.exam.model.Grade;
import org.exam.java.exam.model.User;
import org.exam.java.exam.service.ExamService;
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
@RequestMapping("/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model, Authentication auth) {

        try {
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            model.addAttribute("examsGraded", examService.findUserExamsWithGrade(userId));
            model.addAttribute("examsCancelled", examService.findUserExamsCancelled(userId));
            model.addAttribute("examsToDo", examService.findUserExamsToDo(userId));

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "/main/error";
        }

        return "/exam/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model, Authentication auth) {

        try {
            Exam exam = examService.getById(id);
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            if (exam.getCourse().getUser().getId().equals(userId)) {
                model.addAttribute("exam", exam);
            } else {
                throw new EntityNotFoundException();
            }

        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Exam");
            return "/main/notfound";
        }

        return "/exam/show";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("exam") Exam formExam, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/exam/form";
        }

        examService.create(formExam);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, Authentication auth) {

        try {
            Exam exam = examService.getById(id);
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            if (exam.getCourse().getUser().getId().equals(userId)) {
                model.addAttribute("edit", true);
                model.addAttribute("exam", exam);
            } else {
                throw new EntityNotFoundException();
            }

        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Exam");
            return "/main/notfound";
        }

        return "/exam/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("exam") Exam formExam,
            BindingResult bindingResult, Model model, Authentication auth) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            model.addAttribute("exam", formExam);
            return "/exam/form";
        }

        try {
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();

            if (formExam.getCourse().getUser().getId().equals(userId)) {
                examService.update(formExam);
            } else {
                throw new EntityNotFoundException();
            }

        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Exam");
            return "/main/notfound";
        }

        return "redirect:/exams/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        examService.deleteById(id);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/grade")
    public String createGrade(@PathVariable Integer id, Model model, Authentication auth) {

        try {
            Grade grade = new Grade();
            Optional<User> user = userService.findByUsername(auth.getName());
            Integer userId = user.get().getId();
            grade.setExam(examService.getById(id));

            if (grade.getExam().getCourse().getUser().getId().equals(userId)) {
                model.addAttribute("grade", grade);
            } else {
                throw new EntityNotFoundException();
            }

        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Exam");
            return "/main/notfound";
        }

        return "/grade/form";
    }

}
