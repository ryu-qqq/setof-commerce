package com.connectly.partnerAdmin.auth.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserValidator.class)
@Documented
public @interface UserValidate {
    String message() default "Invalid user details";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
