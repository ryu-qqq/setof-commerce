package com.connectly.partnerAdmin.module.common.validator;

import com.connectly.partnerAdmin.module.common.exception.CommonErrorConstant;
import com.connectly.partnerAdmin.module.common.annotation.ValidDateRange;
import com.connectly.partnerAdmin.module.common.exception.InvalidDateRangeException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;

@Slf4j
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
            PropertyDescriptor startDescriptor = new PropertyDescriptor(startFieldName, value.getClass());
            PropertyDescriptor endDescriptor = new PropertyDescriptor(endFieldName, value.getClass());

            LocalDateTime startDate = (LocalDateTime) startDescriptor.getReadMethod().invoke(value);
            LocalDateTime endDate = (LocalDateTime) endDescriptor.getReadMethod().invoke(value);

            if (startDate == null || endDate == null) {
                throw new InvalidDateRangeException(CommonErrorConstant.INVALID_DATE_NULL_ERROR_MSG);
            }

            if (!startDate.isBefore(endDate)) {
                throw new InvalidDateRangeException(CommonErrorConstant.INVALID_DATE_RANGE_MSG);
            }

            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new InvalidDateRangeException(CommonErrorConstant.INVALID_DATE_MSG);
        }
    }

}
