package com.example.studentresultmanagementsystem.controller;

import com.example.studentresultmanagementsystem.dto.AddStudent;
import com.example.studentresultmanagementsystem.dto.ResponsePage;
import com.example.studentresultmanagementsystem.entity.Student;
import com.example.studentresultmanagementsystem.exceptions.AlreadyExistsException;
import com.example.studentresultmanagementsystem.exceptions.NotFoundException;
import com.example.studentresultmanagementsystem.service.StudentService;
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

//StudentController is responsible for handling queries and mutations related to Student entity
@Controller
@Validated
public class StudentController {
    private final StudentService studentService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    public StudentController(StudentService studentService) {
        logger.info("Initializing StudentController");
        this.studentService = studentService;
    }

    //QueryMapping for fetching students. It can fetch by id or return all students with pagination.
    @QueryMapping
    public ResponsePage students(@Argument Long id, @Argument @Min(0) Integer page, @Argument @Min(1) Integer size)
            throws NotFoundException {
        logger.debug("Fetching students with id: {}, page: {}, size: {}", id, page, size);
        if( id !=null ){
            return studentService.getStudent(id).map(
                    student ->
                         new ResponsePage(
                                Collections.singletonList(student),
                                1L,
                                1,
                                1,
                                0
                         )
            ).orElseThrow(() -> new NotFoundException("Student Not Found!"));
        }
        return new ResponsePage(studentService.getStudents(page, size));
    }

    //MutationMapping for adding a new student and it also validates the input.
    @MutationMapping
    public Student addStudent(@Argument("input") @Valid AddStudent addStudent) throws AlreadyExistsException {
        logger.debug("Adding student with email: {}", addStudent.getEmail());
        return studentService.saveStudent(addStudent);
    }

    //MutationMapping for deleting an existing student.
    @MutationMapping
    public Boolean deleteStudent(@Argument Long id) throws NotFoundException {
        logger.debug("Deleting student with id: {}", id);
        studentService.deleteStudent(id);
        return true;
    }
}
