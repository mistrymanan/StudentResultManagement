package com.example.studentresultmanagementsystem.service;

import com.example.studentresultmanagementsystem.dto.CreateCourse;
import com.example.studentresultmanagementsystem.entity.Course;
import com.example.studentresultmanagementsystem.exceptions.AlreadyExistsException;
import com.example.studentresultmanagementsystem.exceptions.NotFoundException;
import com.example.studentresultmanagementsystem.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//CourserService contains business logic for fetching, creating and deleting courses.
@Service
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepository;
    private final ResultService resultService;

    public CourseService(CourseRepository courseRepository, ResultService resultService) {
        logger.info("Initializing CourseService");
        this.courseRepository = courseRepository;
        this.resultService = resultService;
    }

    public Page<Course> getCourses(int page, int size){
        logger.debug("Fetching courses for page: {} with size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses = courseRepository.findAllByDeletedFalse(pageable);
        logger.info("Found {} courses", courses.getTotalElements());
        return courses;
    }

    public Optional<Course> getCourse(Long id){
        logger.debug("Fetching course with id: {}", id);
        Optional<Course> course = courseRepository.findByIdAndDeletedFalse(id);
        if (course.isPresent()) {
            logger.info("Course found with id: {}", id);
        } else {
            logger.warn("Course not found with id: {}", id);
        }
        return course;
    }

    public Course createCourse(CreateCourse createCourse) throws AlreadyExistsException {
        logger.debug("Creating course with name: {}", createCourse.getName());
        logger.info("Checking if course with name {} already exists", createCourse.getName());
        if( !courseRepository.existsByNameAndDeletedFalse(createCourse.getName()) ){
            logger.info("Course with name {} does not exist, creating new course", createCourse.getName());
            Course course = courseRepository.save(new Course(createCourse.getName()));
            logger.info("Course created successfully with id: {}", course.getId());
            return course;
        }
        logger.warn("Course with name {} already exists", createCourse.getName());
        throw new AlreadyExistsException("Course Already Exists!");
    }

    //Delete course by id and also deletes results associated with the course.
    //Made this method transactional to ensure that both operations are atomic.
    @Transactional
    public void deleteCourse(Long id) throws NotFoundException {
        logger.debug("Deleting course with id: {}", id);
        logger.info("Checking if course with id {} exists", id);
        if( !courseRepository.existsByIdAndDeletedFalse(id) ){
            logger.warn("Course not found with id: {}", id);
            throw new NotFoundException("Course Not Found!");
        }
        logger.info("Course found with id: {}, proceeding to delete", id);
        logger.debug("Deleting results associated with course id: {}", id);
        resultService.deleteResultsByCourseId(id);
        logger.info("Deleting course with id: {}", id);
        courseRepository.deleteById(id);
        logger.info("Course with id: {} deleted successfully", id);
    }
}
