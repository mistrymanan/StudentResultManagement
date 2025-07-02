package com.example.studentresultmanagementsystem.controller;

import com.example.studentresultmanagementsystem.dto.AddResult;
import com.example.studentresultmanagementsystem.dto.ResponsePage;
import com.example.studentresultmanagementsystem.entity.Result;
import com.example.studentresultmanagementsystem.exceptions.AlreadyExistsException;
import com.example.studentresultmanagementsystem.exceptions.NotFoundException;
import com.example.studentresultmanagementsystem.service.ResultService;
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

//ResultController is responsible for handling queries and mutations related to Result entity
@Controller
@Validated
public class ResultController {
    private static final Logger logger = LoggerFactory.getLogger(ResultController.class);
    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        logger.info("Initializing ResultController");
        this.resultService = resultService;
    }

    //QueryMapping for fetching results. it can fetch by id or courseId, or return all results with pagination.
    @QueryMapping
    public ResponsePage results(@Argument Long id, @Argument Long courseId, @Argument @Min(0) Integer page, @Argument @Min(1) Integer size)
            throws NotFoundException {
        logger.debug("Fetching results with id: {}, courseId: {}, page: {}, size: {}", id, courseId, page, size);
        if( id != null ){
            return resultService.getResult(id).map(
                    result ->
                            new ResponsePage(
                                    Collections.singletonList(result),
                                    1L,
                                    1,
                                    1,
                                    0
                            )
            ).orElseThrow(() -> new NotFoundException("Result Not Found!"));
        }else if(courseId != null){
            return new ResponsePage(resultService.getResultByCourseId(courseId, page, size));
        }
        return new ResponsePage(resultService.getResult(page, size));
    }

    //MutationMapping for adding a new result. it also validates the input.
    @MutationMapping
    public Result addResult(@Valid @Argument("input") AddResult addResult) throws AlreadyExistsException, NotFoundException {
        logger.debug("Adding result for studentId: {}, courseId: {}", addResult.getStudentId(), addResult.getCourseId());
        return resultService.saveResult(addResult);
    }

    //MutationMapping for deleting an existing result.
    @MutationMapping
    public Boolean deleteResult(@Argument Long id) throws NotFoundException {
        logger.debug("Deleting result with id: {}", id);
        resultService.deleteResult(id);
        return true;
    }
}
