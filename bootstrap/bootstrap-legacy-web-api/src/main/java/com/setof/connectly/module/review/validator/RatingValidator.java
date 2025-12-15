package com.setof.connectly.module.review.validator;

import com.setof.connectly.module.review.annotation.ValidRating;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RatingValidator implements ConstraintValidator<ValidRating, Double> {

    @Override
    public boolean isValid(Double rating, ConstraintValidatorContext context) {
        if (rating == null) {
            return true; // Null 값은 @NotNull 애너테이션으로 처리
        }
        return rating >= 0 && rating <= 5 && hasOnlyOneDecimalPlace(rating);
    }

    private boolean hasOnlyOneDecimalPlace(double number) {
        double decimalPart = number - (int) number;
        int decimalDigits = (int) (decimalPart * 10);
        return decimalPart * 10 == decimalDigits;
    }
}
