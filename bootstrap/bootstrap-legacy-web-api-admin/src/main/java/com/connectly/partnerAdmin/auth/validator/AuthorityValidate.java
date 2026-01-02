package com.connectly.partnerAdmin.auth.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuthorityValidator.class)
@Documented
public @interface AuthorityValidate {
    String message() default "Invalid user details";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
