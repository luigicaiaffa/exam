package org.exam.java.exam.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.User;
import org.exam.java.exam.repository.RoleRepository;
import org.exam.java.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/users")
    public ResponseEntity<?> index(Authentication auth) {
        try {
            List<User> users = new ArrayList<>();
            Optional<User> user = userService.findByUsername(auth.getName());

            if (user.isEmpty()) {
                return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
            }

            return new ResponseEntity<List<User>>(users, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Integer id, Authentication auth) {

        try {
            Optional<User> user = userService.findByUsername(auth.getName());

            if (user.isEmpty()) {
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
            }

            Integer userId = user.get().getId();

            if (id.equals(userId)) {
                return new ResponseEntity<User>(userService.getById(id), HttpStatus.OK);
            } else {
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid User user, BindingResult bindingResult,
            Authentication auth) {

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            bindingResult.addError(new FieldError("user", "email", "Email is already used"));
        }

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            bindingResult.addError(new FieldError("user", "username", "student ID is already used"));
        }

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.toList());
            return new ResponseEntity<List<String>>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            User newUser = new User();

            newUser.setName(user.getName());
            newUser.setSurname(user.getSurname());
            newUser.setUsername(user.getUsername());
            newUser.setEmail(user.getEmail());
            newUser.setTotalCfu(user.getTotalCfu());
            newUser.setDegreeCourse(user.getDegreeCourse());
            newUser.setRoles(List.of(roleRepository.findByName("USER")));
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));

            return new ResponseEntity<User>(userService.create(newUser), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid User formUser,
            BindingResult bindingResult, Authentication auth) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.toList());
            return new ResponseEntity<List<String>>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<User> user = userService.findByUsername(auth.getName());

            if (user.isEmpty()) {
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
            }

            Integer userId = user.get().getId();
            User existingUser = userService.getById(id);

            if (existingUser == null) {
                return new ResponseEntity<>("Nessun utente trovato con id: " + id, HttpStatus.NOT_FOUND);
            }

            if (id.equals(userId)) {
                existingUser.setName(formUser.getName());
                existingUser.setSurname(formUser.getSurname());
                existingUser.setUsername(formUser.getUsername());
                existingUser.setEmail(formUser.getEmail());
                existingUser.setTotalCfu(formUser.getTotalCfu());
                existingUser.setDegreeCourse(formUser.getDegreeCourse());
                existingUser.setRoles(List.of(roleRepository.findByName("USER")));
                existingUser.setPassword(passwordEncoder.encode(formUser.getPassword()));

                return new ResponseEntity<User>(userService.update(existingUser), HttpStatus.OK);
            } else {
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
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

            if (id.equals(userId)) {
                userService.deleteById(id);
                return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Nessun corso trovato con id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
