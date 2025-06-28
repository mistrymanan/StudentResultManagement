package com.example.studentresultmanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//Result is the entity class that maps to the "results" table in the database.
@Entity
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student cannot be Null")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Course cannot be Null")
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Grade cannot be Null")
    private Score score;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Result() {
    }

    public Result(Student student, Course course, Score score) {
        this.student = student;
        this.course = course;
        this.score = score;
    }
}
