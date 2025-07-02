package com.example.studentresultmanagementsystem.service;

import com.example.studentresultmanagementsystem.dto.AddResult;
import com.example.studentresultmanagementsystem.entity.Course;
import com.example.studentresultmanagementsystem.entity.Result;
import com.example.studentresultmanagementsystem.entity.Student;
import com.example.studentresultmanagementsystem.exceptions.AlreadyExistsException;
import com.example.studentresultmanagementsystem.exceptions.NotFoundException;
import com.example.studentresultmanagementsystem.repository.CourseRepository;
import com.example.studentresultmanagementsystem.repository.ResultRepository;
import com.example.studentresultmanagementsystem.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//ResultService contains business logic for fetching, creating, and deleting results.
@Service
public class ResultService {
    private static final Logger logger = LoggerFactory.getLogger(ResultService.class);
    private final ResultRepository resultRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public ResultService(ResultRepository resultRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        logger.info("Initializing ResultService");
        this.resultRepository   = resultRepository;
        this.studentRepository  = studentRepository;
        this.courseRepository   = courseRepository;
    }

    public Page<Result> getResult(int page, int size){
        logger.debug("Fetching results for page: {} with size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Result> results = resultRepository.findAllByDeletedFalse(pageable);
        logger.info("Found {} results", results.getTotalElements());
        return results;
    }

    public Page<Result> getResultByCourseId(Long courseId,int page, int size) throws NotFoundException {
        logger.debug("Fetching results for courseId: {} on page: {} with size: {}", courseId, page, size);
        if ( !courseRepository.existsByIdAndDeletedFalse(courseId) ){
            logger.warn("Course not found with id: {}", courseId);
            throw new NotFoundException("Course Not Found with id: " + courseId);
        }
        logger.info("Fetching results for courseId: {}", courseId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Result> results = resultRepository.findAllByCourseIdAndDeletedFalse(courseId,pageable);
        logger.info("Found {} results for courseId: {}", results.getTotalElements(), courseId);
        return  results;
    }

    public Optional<Result> getResult(Long id){
        return resultRepository.findByIdAndDeletedFalse(id);
    }

    public Result saveResult(AddResult addResult) throws AlreadyExistsException, NotFoundException {
        logger.debug("Attempting to save result for courseId: {} and studentId: {}", addResult.getCourseId(), addResult.getStudentId());
        if ( !resultRepository.existsByCourseIdAndStudentIdAndDeletedFalse(
            addResult.getCourseId(), addResult.getStudentId()) ) {
            Student student = studentRepository
                    .findByIdAndDeletedFalse(addResult.getStudentId())
                    .orElseThrow(
                            () -> new NotFoundException("Student Not Found with id: " + addResult.getStudentId()));
            Course course = courseRepository
                    .findByIdAndDeletedFalse(addResult.getCourseId())
                    .orElseThrow(
                            () -> new NotFoundException("Course Not Found with id: " + addResult.getCourseId()));
            Result savedResult = resultRepository.save(new Result(student, course, addResult.getScore()));

            logger.info("Successfully saved result with id: {}", savedResult.getId());
            return savedResult;
        }
        logger.warn("Result already exists for courseId: {} and studentId: {}", addResult.getCourseId(), addResult.getStudentId());
        throw new AlreadyExistsException("Result Already Exists!");
    }

    //The Method deletes all results associated with a course
    //Made this method transactional to ensure that the operation is atomic.
    @Transactional
    public void deleteResultsByCourseId(Long courseId) throws NotFoundException {
        logger.debug("Attempting to delete results for courseId: {}", courseId);
        if( ! courseRepository.existsByIdAndDeletedFalse(courseId) ){
            logger.warn("Course not found with id: {}", courseId);
            throw new NotFoundException("Course Not Found with id: " + courseId);
        }
        logger.info("Deleting results for courseId: {}", courseId);
        resultRepository.deleteResultsByCourseId(courseId);
        logger.info("Successfully deleted results for courseId: {}", courseId);
    }

    //The Method deletes all results associated with a student
    //Made this method transactional to ensure that the operation is atomic.
    @Transactional
    public void deleteResultsByStudentId(Long studentId) throws NotFoundException {
        logger.debug("Attempting to delete results for studentId: {}", studentId);
        if( ! studentRepository.existsByIdAndDeletedFalse(studentId) ){
            logger.warn("Student not found with id: {}", studentId);
            throw new NotFoundException("Student Not Found with id: " + studentId);
        }
        logger.info("Deleting results for studentId: {}", studentId);
        resultRepository.deleteResultsByStudentId(studentId);
        logger.info("Successfully deleted results for studentId: {}", studentId);
    }

    public void deleteResult(Long id) throws NotFoundException {
        logger.debug("Attempting to delete result with id: {}", id);
        if( !resultRepository.existsByIdAndDeletedFalse(id) ){
            logger.warn("Result not found with id: {}", id);
            throw new NotFoundException("Result Not Found!");
        }
        logger.info("Deleting result with id: {}", id);
        resultRepository.deleteById(id);
        logger.info("Successfully deleted result with id: {}", id);
    }
}
