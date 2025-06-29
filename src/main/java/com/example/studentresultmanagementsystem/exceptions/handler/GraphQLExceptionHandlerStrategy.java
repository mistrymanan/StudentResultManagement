package com.example.studentresultmanagementsystem.exceptions.handler;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

//This interface defines a strategy for handling exceptions.
public interface GraphQLExceptionHandlerStrategy {
    boolean supports(Throwable ex);
    GraphQLError handle(Throwable ex, DataFetchingEnvironment environment);
}
