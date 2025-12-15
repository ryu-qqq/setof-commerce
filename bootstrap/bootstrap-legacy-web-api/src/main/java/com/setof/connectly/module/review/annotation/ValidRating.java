package com.setof.connectly.module.review.annotation;

import com.setof.connectly.module.review.validator.RatingValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RatingValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRating {
    String message() default "유효하지 않은 점수입니다. 0 과 5사이의 수를 넣어주세요";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
