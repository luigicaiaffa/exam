package org.exam.java.exam.controller;

import java.util.List;

import org.exam.java.exam.model.User;
import org.exam.java.exam.repository.RoleRepository;
import org.exam.java.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/users")
    public String index(Model model) {

        model.addAttribute("users", userService.findAll());
        return "/user/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable Integer id) {

        try {
            User user = userService.getById(id);
            model.addAttribute("user", user);
        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "User");
            return "/main/notfound";
        }

        return "/user/show";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("success", false);
        return "/user/form";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("user") User formUser, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/user/form";
        }

        if (userService.findByUsername(formUser.getUsername()).isPresent()) {
            bindingResult.addError(new FieldError("formUser", "username", "Username is already used"));
            return "/user/form";
        }

        if (userService.findByEmail(formUser.getEmail()).isPresent()) {
            bindingResult.addError(new FieldError("formUser", "email", "Email is already used"));
            return "/user/form";
        }

        try {
            User newUser = new User();

            newUser.setName(formUser.getName());
            newUser.setSurname(formUser.getSurname());
            newUser.setUsername(formUser.getUsername());
            newUser.setEmail(formUser.getEmail());
            newUser.setTotalCfu(formUser.getTotalCfu());
            newUser.setDegreeCourse(formUser.getDegreeCourse());
            newUser.setRoles(List.of(roleRepository.findByName("USER")));
            newUser.setPassword(passwordEncoder.encode(formUser.getPassword()));

            userService.create(newUser);

            model.addAttribute("user", new User());
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "main/error";
        }

        return "/user/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {

        try {
            model.addAttribute("edit", true);
            model.addAttribute("user", userService.getById(id));
        } catch (EntityNotFoundException e) {
            model.addAttribute("element", "User");
            return "/main/notfound";
        }

        return "/user/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("user") User formUser,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            model.addAttribute("user", formUser);
            return "/user/form";
        }

        formUser.setPassword(passwordEncoder.encode(formUser.getPassword()));
        userService.update(formUser);
        return "redirect:/user/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        userService.deleteById(id);
        return "redirect:/home";
    }

}
