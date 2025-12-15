package com.setof.connectly.module.user.annotation;

import com.setof.connectly.module.user.validator.ConsentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConsentValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsentValid {

    String message() default "서비스 이용약관 및 개인정보 수집 동의는 서비스를 이용하기 위한 필수 동의 사항 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
