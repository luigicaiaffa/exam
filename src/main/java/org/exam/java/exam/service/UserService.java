package org.exam.java.exam.service;

import java.util.List;
import java.util.Optional;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.User;
import org.exam.java.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public User getById(Integer id) {
        Optional<User> userAttempt = userRepository.findById(id);

        if (userAttempt.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return userAttempt.get();
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        for (Course course : user.getCourses()) {
            courseService.delete(course);
        }

        userRepository.delete(user);
    }

    public void deleteById(Integer id) {
        User user = getById(id);

        for (Course course : user.getCourses()) {
            courseService.delete(course);
        }

        userRepository.delete(user);
    }

    public Boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    public Boolean exists(User user) {
        return userRepository.existsById(user.getId());
    }
}
