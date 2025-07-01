package com.example.studentresultmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//CreateCourse is a Data Transfer Object (DTO) for creating a new course.
public class CreateCourse {
    @NotNull(message = "Course Name must not be Null")
    @NotBlank(message = "Course Name must not be Blank")
    @Size(min = 3, max = 50, message = "Course name should be of 3 to 50 Characters")
    String name;

    public CreateCourse() {
    }

    public CreateCourse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public CreateCourse setName(String name) {
        this.name = name;
        return this;
    }
}
