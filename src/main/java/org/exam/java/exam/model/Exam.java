package org.exam.java.exam.model;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "exams")
public class Exam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Exam date cannot be null")
    private LocalDateTime date; 

    @NotBlank(message = "Exam location cannot be blank")
    private String location; 

    @Lob
    private String notes;

    @NotNull
    private Boolean isCancelled;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToOne(mappedBy = "exam")
    private Grade grade;

    // # Getters / Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    // # Constructors
    public Exam() {
    }

    public Exam(@NotNull(message = "Exam date cannot be null") LocalDateTime date,
            @NotBlank(message = "Exam location cannot be blank") String location, String notes, Course course,
            Grade grade, Boolean isCancelled) {
        this.date = date;
        this.location = location;
        this.notes = notes;
        this.course = course;
        this.grade = grade;
        this.isCancelled = isCancelled;
    }

    // # Methods
    @Override
    public String toString() {
        return this.date + " - " + this.location;
    }

}

