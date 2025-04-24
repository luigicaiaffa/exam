package org.exam.java.exam.model;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    // # Constructors
    public Exam() {
    }

    public Exam(@NotNull(message = "Exam date cannot be null") LocalDateTime date,
            @NotBlank(message = "Exam location cannot be blank") String location, String notes) {
        this.date = date;
        this.location = location;
        this.notes = notes;
    }
    
    // # Methods
    @Override
    public String toString() {
        return "Exam [date=" + date + ", location=" + location + "]";
    } 


}

