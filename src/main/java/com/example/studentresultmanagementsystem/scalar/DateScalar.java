package com.example.studentresultmanagementsystem.scalar;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

//DateScalar is a custom GraphQL scalar type for handling LocalDate in the format MM/dd/yyyy.
public class DateScalar {
    private static final Logger logger = LoggerFactory.getLogger(DateScalar.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static GraphQLScalarType getDateScalar(){
        logger.info("Creating Date Scalar type with format MM/dd/yyyy");
        return GraphQLScalarType.newScalar()
                .name("Date")
                .description("Date Scalar type to handle MM/dd/yyyy")
                .coercing(new Coercing<LocalDate, String>() {
                    @Override
                    public @Nullable String serialize(@NonNull Object dataFetcherResult, @NonNull GraphQLContext graphQLContext, @NonNull Locale locale) throws CoercingSerializeException {
                        logger.debug("Serializing LocalDate to String using format: {}", DATE_TIME_FORMATTER);
                        if (dataFetcherResult instanceof  LocalDate date){
                            logger.debug("Serialized LocalDate: {}", date);
                            return DATE_TIME_FORMATTER.format(date);
                        }
                        logger.error("Expected a LocalDate object, but received: {}", dataFetcherResult.getClass().getName());
                        throw new CoercingSerializeException("Expected a LocalDate object");
                    }

                    @Override
                    public @Nullable LocalDate parseValue(@NonNull Object input, @NonNull GraphQLContext graphQLContext, @NonNull Locale locale) throws CoercingParseValueException {
                        logger.debug("Parsing input value to LocalDate: {}", input);
                        if ( input instanceof String date){
                            logger.debug("Received date string: {}", date);
                            try {
                                logger.debug("Parsing date string using format: {}", DATE_TIME_FORMATTER);
                                LocalDate parsedDate = LocalDate.parse(date, DATE_TIME_FORMATTER);
                                logger.debug("Parsed LocalDate: {}", parsedDate);
                                return parsedDate;
                            } catch (DateTimeParseException e){
                                logger.error("Date parsing failed for input: {}. Expected format is MM/dd/yyyy", date, e);
                                throw new CoercingParseValueException("Received Invalid Date format, Expected 'MM/dd/yyyy'");
                            }
                        }
                        logger.error("Expected a String value for Date, but received: {}", input.getClass().getName());
                        throw new CoercingParseValueException("Expected a String value for Date");
                    }

                    @Override
                    public @Nullable LocalDate parseLiteral(@NonNull Value<?> input, @NonNull CoercedVariables variables, @NonNull GraphQLContext graphQLContext, @NonNull Locale locale) throws CoercingParseLiteralException {
                        logger.debug("Parsing literal input to LocalDate: {}", input);
                        if ( input instanceof StringValue stringValue){
                            logger.debug("Received StringValue: {}", stringValue.getValue());
                            try {
                                logger.debug("Parsing StringValue using format: {}", DATE_TIME_FORMATTER);
                                LocalDate parsedDate = LocalDate.parse(stringValue.getValue(), DATE_TIME_FORMATTER);
                                logger.debug("Parsed LocalDate from StringValue: {}", parsedDate);
                                return parsedDate;
                            } catch (DateTimeParseException e){
                                logger.error("Date parsing failed for StringValue: {}. Expected format is MM/dd/yyyy", stringValue.getValue(), e);
                                throw new CoercingParseLiteralException("Received Invalid Date format, Expected 'MM/dd/yyyy'");
                            }
                        }
                        logger.error("Expected a StringValue for Date literal, but received: {}", input.getClass().getName());
                        throw new CoercingParseValueException("Expected a StringValue for Date literal.");
                    }
                })
                .build();
    }
}
