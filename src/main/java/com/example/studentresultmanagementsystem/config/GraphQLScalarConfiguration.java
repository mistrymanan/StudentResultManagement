package com.example.studentresultmanagementsystem.config;

import com.example.studentresultmanagementsystem.scalar.DateScalar;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.RuntimeWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

//The class configures the Custom Datatype.
@Configuration
public class GraphQLScalarConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(GraphQLScalarConfiguration.class);

    @Bean
    public GraphQLScalarType localDateScalar() {
        logger.info("Creating LocalDate GraphQL Scalar Type");
        return DateScalar.getDateScalar();
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(GraphQLScalarType date) {
        logger.info("Configuring RuntimeWiring with LocalDate Scalar Type");
//        return new RuntimeWiringConfigurer() {
//            @Override
//            public void configure(RuntimeWiring.Builder builder) {
//                builder.scalar(date);
//            }
//        };
        RuntimeWiringConfigurer runtimeWiringConfigurer = builder -> builder.scalar(date);
        logger.info("RuntimeWiringConfigurer created with LocalDate Scalar Type");
        return runtimeWiringConfigurer;
    }
}
