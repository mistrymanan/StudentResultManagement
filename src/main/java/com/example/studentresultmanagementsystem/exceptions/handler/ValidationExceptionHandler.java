package com.example.studentresultmanagementsystem.exceptions.handler;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

//ValidationExceptionHandler provides strategy to handle ConstraintViolationException.
@Component
public class ValidationExceptionHandler implements GraphQLExceptionHandlerStrategy {
    private static final Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    @Override
    public boolean supports(Throwable ex) {
        boolean supports = ex instanceof ConstraintViolationException;
        logger.error("Supports method called with exception: {}, supports: {}", ex.getMessage(), supports);
        return supports;
    }

    @Override
    public GraphQLError handle(Throwable ex, DataFetchingEnvironment environment) {
        logger.error("Handle method called with exception: {}", ex.getMessage());
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex;

        String response = constraintViolationException.getConstraintViolations()
                .stream()
                .map(constraintViolation -> {
                    String propertyPath = constraintViolation.getPropertyPath().toString();
                    String message = constraintViolation.getMessage();
                    return propertyPath + ": " + message + "\n";
                })
                .collect(Collectors.joining());
        logger.error("Validation errors: {}", response);
        return  GraphqlErrorBuilder.newError()
                .message(response)
                .errorType(ErrorType.BAD_REQUEST)
                .build();
    }
}
