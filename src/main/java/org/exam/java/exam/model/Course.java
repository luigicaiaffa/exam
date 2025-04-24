package org.exam.java.exam.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Course name cannot be blank")
    private String name;

    @Positive(message = "Number of CFU must be positive")
    @NotNull(message = "Number of CFU cannot be null")
    private Integer cfu;
    
    @Positive(message = "Course year must be a positive number")
    @NotNull(message = "Course year cannot be null")
    private Integer courseYear;
    
    @NotNull
    private Boolean isOptional;
    
    @NotNull
    private Boolean isPassed;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "course")
    private List<Grade> grades;

    @OneToMany(mappedBy = "course")
    private List<Exam> exams;

    // # Getters / Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCfu() {
        return cfu;
    }

    public void setCfu(Integer cfu) {
        this.cfu = cfu;
    }

    public Integer getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(Integer courseYear) {
        this.courseYear = courseYear;
    }

    public Boolean getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(Boolean isOptional) {
        this.isOptional = isOptional;
    }

    public Boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Boolean isPassed) {
        this.isPassed = isPassed;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    // # Constructors
    public Course() {
    }
    
    public Course(@NotBlank(message = "Exam name cannot be blank") String name,
    @Positive(message = "Number of CFU must be positive") @NotNull(message = "Number of CFU cannot be null") Integer cfu,
    @Positive(message = "Course year must be a positive number") @NotNull(message = "Course year cannot be null") Integer courseYear,
    @NotNull Boolean isOptional, @NotNull Boolean isPassed) {
        this.name = name;
        this.cfu = cfu;
        this.courseYear = courseYear;
        this.isOptional = isOptional;
        this.isPassed = isPassed;
    }

    // # Methods
    @Override
    public String toString() {
        return "Course [name=" + name + "]";
    }


}
