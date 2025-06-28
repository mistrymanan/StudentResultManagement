package com.example.studentresultmanagementsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;

//The AgeValidator class implements the ConstraintValidator interface to validate
//if a given date meets the minimum age requirement.
public class AgeValidator implements ConstraintValidator<MinimumAge, LocalDate> {
    private static final Logger logger = LoggerFactory.getLogger(AgeValidator.class);
    private int minimumAge;

    @Override
    public void initialize(MinimumAge constraintAnnotation) {
        logger.info("Initializing AgeValidator with minimum age: {}", constraintAnnotation.value());
        this.minimumAge = constraintAnnotation.value();
        logger.debug("Minimum age set to: {}", this.minimumAge);
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        logger.debug("Validating date: {}", date);
        LocalDate today = LocalDate.now();
        boolean isValid = Period.between(date, today).getYears() >= this.minimumAge;
        logger.info("Date {} is {}valid for minimum age {}", date, isValid ? "" : "not ", this.minimumAge);
        if (!isValid) {
            logger.warn("Validation failed for date: {}. Age is less than minimum required age: {}", date, this.minimumAge);
        }
        return isValid;
    }
}
