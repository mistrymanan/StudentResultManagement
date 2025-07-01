package com.example.studentresultmanagementsystem.service;

import com.example.studentresultmanagementsystem.dto.AddStudent;
import com.example.studentresultmanagementsystem.entity.Student;
import com.example.studentresultmanagementsystem.exceptions.AlreadyExistsException;
import com.example.studentresultmanagementsystem.exceptions.NotFoundException;
import com.example.studentresultmanagementsystem.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//StudentService contains business logic for fetching, creating, and deleting students.
@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final ResultService resultService;
    private final ModelMapper modelMapper;

    public StudentService(StudentRepository studentRepository, ResultService resultService, ModelMapper modelMapper) {
        logger.info("Initializing StudentService");
        this.studentRepository = studentRepository;
        this.resultService = resultService;
        this.modelMapper = modelMapper;
    }

    public Page<Student> getStudents(int page, int size){
        logger.debug("Fetching students for page: {} with size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentRepository.findAllByDeletedFalse(pageable);
        logger.info("Found {} students", students.getTotalElements());
        return students;
    }

    public Optional<Student> getStudent(Long id){
        logger.debug("Fetching student with id: {}", id);
        Optional<Student> student = studentRepository.findByIdAndDeletedFalse(id);
        if (student.isPresent()) {
            logger.info("Found student with id: {}", id);
        } else {
            logger.warn("Student not found with id: {}", id);
        }
        return student;
    }

    public Student saveStudent(AddStudent addStudent) throws AlreadyExistsException {
        logger.debug("Attempting to save student with email: {}", addStudent.getEmail());
        logger.info("Checking if student with email {} already exists", addStudent.getEmail());
        if (!studentRepository.existsByEmailAndDeletedFalse(addStudent.getEmail())) {
            logger.info("Student with email {} does not exist, adding student", addStudent.getEmail());
            return studentRepository.save(modelMapper.map(addStudent, Student.class));
        }
        logger.error("Student with email {} already exists", addStudent.getEmail());
        throw new AlreadyExistsException("Student Already Exists with!");
    }

    @Transactional
    public void deleteStudent(Long id) throws NotFoundException {
        logger.debug("Attempting to delete student with id: {}", id);
        logger.info("Checking if student with id {} exists", id);
        if( !studentRepository.existsByIdAndDeletedFalse(id) ){
            logger.error("Student with id {} not found", id);
            throw new NotFoundException("Student Not Found!");
        }
        logger.debug("Deleting results associated with student id: {}", id);
        resultService.deleteResultsByStudentId(id);
        logger.info("Deleting student with id: {}", id);
        studentRepository.deleteById(id);
        logger.info("Successfully deleted student with id: {}", id);
    }
}
