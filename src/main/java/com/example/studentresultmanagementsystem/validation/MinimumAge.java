package com.example.studentresultmanagementsystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//MinimumAge is a custom validation annotation.
//it checks if a field's value is greater than or equal to a specified minimum age.
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
public @interface MinimumAge {
    String message();
    int value();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
