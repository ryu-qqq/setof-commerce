package com.setof.connectly.module.common.validator;

import com.setof.connectly.module.common.annotation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startFieldName;
    private String endFieldName;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startFieldName = constraintAnnotation.start();
        this.endFieldName = constraintAnnotation.end();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            PropertyDescriptor startDescriptor =
                    new PropertyDescriptor(startFieldName, value.getClass());
            PropertyDescriptor endDescriptor =
                    new PropertyDescriptor(endFieldName, value.getClass());

            LocalDateTime startDate = (LocalDateTime) startDescriptor.getReadMethod().invoke(value);
            LocalDateTime endDate = (LocalDateTime) endDescriptor.getReadMethod().invoke(value);

            if (startDate == null || endDate == null) {
                return false;
            }

            if (!startDate.isBefore(endDate)) {
                return false;
            }

            return true;
        } catch (Exception ex) {

            return false;
        }
    }
}
