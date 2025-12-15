package com.setof.connectly.module.payment.annotation;

import com.setof.connectly.module.payment.validator.PriceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceValidator.class)
public @interface ValidPrice {

    String message() default "결제 금액은 최소 10,000원 이상부터입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
