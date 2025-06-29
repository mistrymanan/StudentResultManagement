package com.example.studentresultmanagementsystem.exceptions.handler;


import com.example.studentresultmanagementsystem.exceptions.AlreadyExistsException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

//AlreadyExistsExceptionHandler provides strategy to handle AlreadyExistsException.
@Component
public class AlreadyExistsExceptionHandler implements GraphQLExceptionHandlerStrategy {
    private static final Logger logger = LoggerFactory.getLogger(AlreadyExistsExceptionHandler.class);

    @Override
    public boolean supports(Throwable ex){
        boolean supports = ex instanceof AlreadyExistsException;
        logger.error("Supports method called with exception: {}, supports: {}", ex.getMessage(), supports);
        return supports;
    }

    @Override
     public GraphQLError handle(Throwable ex, DataFetchingEnvironment environment) {
        logger.error("Handle method called with exception: {}", ex.getMessage());
        return GraphqlErrorBuilder.newError(environment)
                .message(ex.getMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .build();
    }
}
