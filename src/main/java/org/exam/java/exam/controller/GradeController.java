package org.exam.java.exam.controller;

import org.exam.java.exam.model.Grade;
import org.exam.java.exam.service.GradeService;
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
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    public String index(Model model) {

        model.addAttribute("grades", gradeService.findAll());
        return "/grade/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {

        try {
            Grade grade = gradeService.getById(id);
            model.addAttribute("grade", grade);
        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Grade");
            return "/main/notfound";
        }

        return "/grade/show";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("grade", new Grade());
        return "/grade/form";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("grade") Grade formGrade, BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "/grade/form";
        }

        gradeService.create(formGrade);
        return "redirect:/grade/index";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {

        try {
            model.addAttribute("edit", true);
            model.addAttribute("grade", gradeService.getById(id));
            return "/grade/form";
        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "Grade");
            return "/main/notfound";
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("grade") Grade formGrade,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            model.addAttribute("grade", formGrade);
            return "/grade/form";
        }

        gradeService.update(formGrade);
        return "redirect:/grade/index";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        gradeService.deleteById(id);
        return "redirect:/grade/index";
    }

}
