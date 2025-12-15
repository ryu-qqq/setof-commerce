package com.setof.connectly.module.payment.validator;

import com.setof.connectly.module.payment.annotation.ValidMileage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MileageValidator implements ConstraintValidator<ValidMileage, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == 0) {
            return true;
        }
        return value >= 1000;
    }
}
