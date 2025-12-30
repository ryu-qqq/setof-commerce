package com.connectly.partnerAdmin.module.product.annotation;

import com.connectly.partnerAdmin.module.product.validator.CategoryChecker;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryChecker.class)
@Documented
public @interface CategoryValidate {
    String message() default "Invalid Category";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
