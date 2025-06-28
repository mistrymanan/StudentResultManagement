package com.example.studentresultmanagementsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//EmailDomainValidator is a custom validator that checks if an email address ends with one of the allowed domains.
public class EmailDomainValidator implements ConstraintValidator<ValidateEmailDomain, String> {
    private static final Logger logger = LoggerFactory.getLogger(EmailDomainValidator.class);
    private List<String> allowedDomains;

    @Override
    public void initialize(ValidateEmailDomain constraintAnnotation) {
        logger.info("Initializing EmailDomainValidator with allowed domains: {}", (Object[]) constraintAnnotation.allowedDomains());
        this.allowedDomains = Arrays.stream(constraintAnnotation.allowedDomains())
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        logger.debug("Allowed domains set to: {}", allowedDomains);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext){
        logger.debug("Validating email: {}", email);
        if (email == null || email.isBlank()) return false;
        logger.debug("Email is not blank. Checking if it ends with one of the allowed domains: {}", allowedDomains);
        email = email.toLowerCase();
        boolean isValid = allowedDomains.stream()
                .anyMatch(email::endsWith);
        if (isValid) {
            logger.info("Email {} is valid", email);
        } else {
            logger.warn("Email {} is not valid. It does not end with any of the allowed domains: {}", email, allowedDomains);
        }
        return isValid;
    }
}
