package com.chototclone.Rules;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MailRFC.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MailRFCConstraint {
    String message() default "Invalid email address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

