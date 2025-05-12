package org.exam.java.exam.controller.rest;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.exam.java.exam.model.User;
import org.exam.java.exam.service.GradeService;
import org.exam.java.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "http://localhost:5173")
public class GuestRestController {

    @Autowired
    UserService userService;

    @Autowired
    GradeService gradeService;

    @GetMapping("/guest")
    public ResponseEntity<User> getGuestData() {

        Optional<User> guest = userService.findById(999);

        if (guest.isPresent()) {
            return new ResponseEntity<User>(guest.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/averages")
    public ResponseEntity<?> getGuestAverages() {
        Map<String, BigDecimal> averages = gradeService.getAveragesByUserId(999);

        if (!averages.isEmpty()) {
            return new ResponseEntity<Map<String, BigDecimal>>(averages, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
