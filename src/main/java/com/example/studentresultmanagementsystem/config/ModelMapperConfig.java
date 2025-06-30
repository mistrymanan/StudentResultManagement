package com.example.studentresultmanagementsystem.config;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//The class configures the ModelMapper bean.
@Configuration
public class ModelMapperConfig {
    private static final Logger logger = LoggerFactory.getLogger(ModelMapperConfig.class);

    @Bean
    public ModelMapper modelMapper() {
        logger.info("Creating ModelMapper bean");
        return new ModelMapper();
    }
}
