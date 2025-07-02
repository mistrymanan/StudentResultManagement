package com.example.studentresultmanagementsystem.controller;

import com.example.studentresultmanagementsystem.dto.CreateCourse;
import com.example.studentresultmanagementsystem.dto.ResponsePage;
import com.example.studentresultmanagementsystem.entity.Course;
import com.example.studentresultmanagementsystem.exceptions.AlreadyExistsException;
import com.example.studentresultmanagementsystem.exceptions.NotFoundException;
import com.example.studentresultmanagementsystem.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;

//CourseController is responsible for handling queries and mutations related to Course entity
@Controller
@Validated
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        logger.info("Initializing CourseController");
        this.courseService = courseService;
    }

    //QueryMapping for fetching courses
    @QueryMapping
    public ResponsePage courses(@Argument Long id, @Argument @Min(0) Integer page, @Argument @Min(1) Integer size) throws NotFoundException {
        logger.debug("Fetching courses with id: {}, page: {}, size: {}", id, page, size);
        if( id !=null ) {
            return courseService.getCourse(id).map(
                    course ->
                            new ResponsePage(
                                    Collections.singletonList(course),
                                    1L,
                                    1,
                                    1,
                                    0
                            )
            ).orElseThrow(() -> new NotFoundException("Course Not Found!"));
        }
        return new ResponsePage(courseService.getCourses(page, size));
    }

    //MutationMapping for creating a new course
    @MutationMapping
    public Course createCourse(@Valid @Argument("input") CreateCourse createCourse) throws AlreadyExistsException {
        logger.debug("Creating course with name: {}", createCourse.getName());
        return courseService.createCourse(createCourse);
    }

    //MutationMapping for deleting an existing course
    @MutationMapping
    public Boolean deleteCourse(@Argument Long id) throws NotFoundException {
        logger.debug("Deleting course with id: {}", id);
        courseService.deleteCourse(id);
        return true;
    }
}
