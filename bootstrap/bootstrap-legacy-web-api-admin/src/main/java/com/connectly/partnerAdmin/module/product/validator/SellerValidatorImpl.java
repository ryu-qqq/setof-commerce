package com.connectly.partnerAdmin.module.product.validator;

import com.connectly.partnerAdmin.auth.core.UserPrincipal;
import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.connectly.partnerAdmin.module.product.exception.InvalidProductException;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SellerValidatorImpl implements SellerValidator {

    private final static String SELLER_MISMATCH = "Seller ID mismatch.";

    @Override
    public void isValid(long sellerId) {

        UserPrincipal userPrincipal = SecurityUtils.getAuthentication();
        RoleType authorization = SecurityUtils.getAuthorization();

        if (!authorization.isMaster() && !Objects.equals(userPrincipal.sellerId(), sellerId)) {
            throw new InvalidProductException(SELLER_MISMATCH);
        }
    }

}
