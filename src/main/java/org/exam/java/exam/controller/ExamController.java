package org.exam.java.exam.controller;

import org.exam.java.exam.model.Exam;
import org.exam.java.exam.model.Grade;
import org.exam.java.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public String index(Model model) {

        model.addAttribute("examsGraded", examService.findExamsWithGrade());
        model.addAttribute("examsCancelled", examService.findExamsCancelled());
        model.addAttribute("examsToDo", examService.findExamsToDo());
        return "/exam/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {

        try {
            model.addAttribute("exam", examService.getById(id));
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
    public String edit(@PathVariable Integer id, Model model) {

        try {
            model.addAttribute("edit", true);
            model.addAttribute("exam", examService.getById(id));
        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Exam");
            return "/main/notfound";
        }

        return "/exam/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("exam") Exam formExam,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            model.addAttribute("exam", formExam);
            return "/exam/form";
        }

        examService.update(formExam);
        return "redirect:/courses";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        examService.deleteById(id);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/grade")
    public String createGrade(@PathVariable Integer id, Model model) {

        try {
            Grade grade = new Grade();
            grade.setExam(examService.getById(id));
            model.addAttribute("grade", grade);
        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Grade");
            return "/main/notfound";
        }

        return "/grade/form";
    }

}
