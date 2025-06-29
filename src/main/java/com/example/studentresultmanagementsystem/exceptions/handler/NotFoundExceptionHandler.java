package com.example.studentresultmanagementsystem.exceptions.handler;

import com.example.studentresultmanagementsystem.exceptions.NotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

//NotFoundExceptionHandler provides strategy to handle NotFoundException.
@Component
public class NotFoundExceptionHandler implements GraphQLExceptionHandlerStrategy {
    private static final Logger logger = LoggerFactory.getLogger(NotFoundExceptionHandler.class);

    @Override
    public boolean supports(Throwable ex) {
        boolean supports = ex instanceof NotFoundException;
        logger.error("Supports method called with exception: {}, supports: {}", ex.getMessage(), supports);
        return supports;
    }

    @Override
    public GraphQLError handle(Throwable ex, DataFetchingEnvironment environment){
        logger.error("Handle method called with exception: {}", ex.getMessage());
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .errorType(ErrorType.NOT_FOUND)
                .build();
    }
}
