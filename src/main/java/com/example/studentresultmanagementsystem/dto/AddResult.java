package com.example.studentresultmanagementsystem.dto;

import com.example.studentresultmanagementsystem.entity.Score;
import jakarta.validation.constraints.NotNull;

//AddResult is a Data Transfer Object (DTO) for adding a new result.
public class AddResult {
    @NotNull(message = "Course Id cannot be Null")
    private Long courseId;

    @NotNull(message = "Student Id cannot be Null")
    private Long studentId;

    @NotNull(message = "Grade is required")
    private Score score;


    public AddResult() {
    }

    public AddResult(Long courseId, Long studentId, Score score) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.score = score;
    }

    public Long getCourseId() {
        return courseId;
    }

    public AddResult setCourseId(Long courseId) {
        this.courseId = courseId;
        return this;
    }

    public Long getStudentId() {
        return studentId;
    }

    public AddResult setStudentId(Long studentId) {
        this.studentId = studentId;
        return this;
    }

    public Score getScore() {
        return score;
    }

    public AddResult setScore(Score score) {
        this.score = score;
        return this;
    }
}
