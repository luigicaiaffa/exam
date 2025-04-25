package org.exam.java.exam.controller;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String index(Model model) {

        model.addAttribute("courses", courseService.findAll());
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

        courseService.create(formCourse);
        return "redirect:/course/index";
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

        courseService.update(formCourse);
        return "redirect:/course/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {

        courseService.deleteById(id);
        return "redirect:/course/index";
    }
    
}
