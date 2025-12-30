package com.connectly.partnerAdmin.module.product.annotation;

import com.connectly.partnerAdmin.module.product.validator.ProductValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductValidator.class)
@Documented
public @interface ProductValidate {
    String message() default "Invalid Product";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
