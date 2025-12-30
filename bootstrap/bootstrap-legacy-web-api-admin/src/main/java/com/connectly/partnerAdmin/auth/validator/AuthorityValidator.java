package com.connectly.partnerAdmin.auth.validator;

import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.connectly.partnerAdmin.module.common.filter.RoleFilter;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthorityValidator implements ConstraintValidator<AuthorityValidate, RoleFilter> {

    @Override
    public boolean isValid(RoleFilter roleFilter, ConstraintValidatorContext constraintValidatorContext) {
        RoleType authorization = SecurityUtils.getAuthorization();
        if (authorization.isSeller()) {
            roleFilter.setSellerId(SecurityUtils.currentSellerId());
        }
        return true;
    }
}
