package com.setof.connectly.module.payment.annotation;

import com.setof.connectly.module.payment.validator.UserMileageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserMileageValidator.class)
public @interface ValidUserMileage {

    String message() default "마일리지 사용량이 초과하였습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
