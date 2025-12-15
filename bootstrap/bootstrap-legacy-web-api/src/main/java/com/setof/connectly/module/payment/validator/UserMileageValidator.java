package com.setof.connectly.module.payment.validator;

import com.setof.connectly.module.payment.annotation.ValidUserMileage;
import com.setof.connectly.module.payment.dto.payment.BasePayment;
import com.setof.connectly.module.user.service.mileage.UserMileageFindService;
import com.setof.connectly.module.utils.SecurityUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserMileageValidator implements ConstraintValidator<ValidUserMileage, BasePayment> {

    private final UserMileageFindService userMileageFindService;

    @Override
    public boolean isValid(BasePayment value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        long mileageAmount = value.getMileageAmount();
        userMileageFindService.hasMileageEnough(SecurityUtils.currentUserId(), mileageAmount);

        return true;
    }
}
