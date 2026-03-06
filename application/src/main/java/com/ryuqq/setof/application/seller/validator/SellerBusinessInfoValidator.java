package com.ryuqq.setof.application.seller.validator;

import com.ryuqq.setof.application.seller.manager.SellerBusinessInfoReadManager;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.exception.RegistrationNumberDuplicateException;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;

@Component
public class SellerBusinessInfoValidator {

    private final SellerBusinessInfoReadManager sellerBusinessInfoReadManager;

    public SellerBusinessInfoValidator(
            SellerBusinessInfoReadManager sellerBusinessInfoReadManager) {
        this.sellerBusinessInfoReadManager = sellerBusinessInfoReadManager;
    }

    public SellerBusinessInfo findExistingOrThrow(SellerId sellerId) {
        return sellerBusinessInfoReadManager.getBySellerId(sellerId);
    }

    public void validateRegistrationNumberNotDuplicate(String registrationNumber) {
        if (sellerBusinessInfoReadManager.existsByRegistrationNumber(registrationNumber)) {
            throw new RegistrationNumberDuplicateException(registrationNumber);
        }
    }

    public void validateRegistrationNumberNotDuplicateExcluding(
            String registrationNumber, SellerId excludeId) {
        if (sellerBusinessInfoReadManager.existsByRegistrationNumberExcluding(
                registrationNumber, excludeId)) {
            throw new RegistrationNumberDuplicateException(registrationNumber);
        }
    }
}
