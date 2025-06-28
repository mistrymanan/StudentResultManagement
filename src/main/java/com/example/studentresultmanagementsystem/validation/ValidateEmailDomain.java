package com.example.studentresultmanagementsystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//ValidateEmailDomain is a custom validation annotation.
//It checks if an email address ends with one of the allowed domains, such as "gmail.com" or "yahoo.com".
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailDomainValidator.class)
public @interface ValidateEmailDomain{
    String message() default "Email domain is not allowed. Only Gmail.com and Yahoo.com are permitted.";
    String[] allowedDomains() default {"gmail.com", "yahoo.com"};
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
