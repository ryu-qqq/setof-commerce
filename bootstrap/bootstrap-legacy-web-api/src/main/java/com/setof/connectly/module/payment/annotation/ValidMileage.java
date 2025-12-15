package com.setof.connectly.module.payment.annotation;

import com.setof.connectly.module.payment.validator.MileageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MileageValidator.class)
public @interface ValidMileage {
    String message() default "마일리지 최소 사용 금액은 1000원 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
