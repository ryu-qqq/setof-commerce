package com.setof.connectly.module.user.validator;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.user.annotation.ConsentValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConsentValidator implements ConstraintValidator<ConsentValid, Yn> {

    @Override
    public boolean isValid(Yn value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.isYes();
    }
}
