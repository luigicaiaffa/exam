package org.exam.java.exam.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.exam.java.exam.model.Course;
import org.exam.java.exam.model.Exam;
import org.exam.java.exam.model.User;
import org.exam.java.exam.service.CourseService;
import org.exam.java.exam.service.ExamService;
import org.exam.java.exam.service.GradeService;
import org.exam.java.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ExamService examService;

    @Autowired
    private GradeService gradeService;

    @GetMapping("/")
    public String homePage(Authentication auth, Model model) {

        User user = userService.findByUsername(auth.getName()).get();
        Integer userId = user.getId();

        List<Integer> coursesYears = courseService.findUserCoursesSortedByYear(userId).stream()
                .map(Course::getCourseYear).distinct().sorted().collect(Collectors.toList());

        Map<String, BigDecimal> averages = gradeService.getAveragesByUserId(userId);
        List<Exam> examsToDo = examService.findUserExamsToDo(userId);
        List<Exam> examsPassed = examService.findUserExamsWithGrade(userId);

        model.addAttribute("weightedAvg", averages.get("weighted"));
        model.addAttribute("totalCfu", averages.get("totalCfu"));

        model.addAttribute("coursesYears", coursesYears);

        model.addAttribute("user", user);
        model.addAttribute("examsToDo", examsToDo);
        model.addAttribute("examsPassed", examsPassed);

        return "/main/homepage";
    }

    @GetMapping("/login")
    public String login() {

        return "/main/login";
    }

}
