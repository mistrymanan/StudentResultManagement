package com.example.studentresultmanagementsystem.config;

import com.example.studentresultmanagementsystem.exceptions.handler.GraphQLExceptionHandlerStrategy;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

//The class is responsible for handling exceptions
@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GraphQLExceptionHandler.class);

    //Handlers are the list of strategies which are used to handle the exceptions that are being thrown.
    //This list will automatically be injected by Spring using parameterized constructor injection.
    private final List<GraphQLExceptionHandlerStrategy> handlers;

    public GraphQLExceptionHandler(List<GraphQLExceptionHandlerStrategy> handlers) {
        logger.info("Initializing GraphQLExceptionHandler with {} handlers", handlers);
        this.handlers = handlers;
    }

    //This method checks the thrown exceptions and handles based on supported strategy.
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment environment) {
        logger.error("Resolving exception: {}", ex.getMessage());
        return handlers.stream()
                .filter(handler -> handler.supports(ex))
                .findFirst()
                .map(handler -> handler.handle(ex, environment))
                .orElse(super.resolveToSingleError(ex, environment));
    }
}
