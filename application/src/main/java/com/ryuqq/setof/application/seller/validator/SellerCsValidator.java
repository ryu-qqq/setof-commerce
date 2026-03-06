package com.ryuqq.setof.application.seller.validator;

import com.ryuqq.setof.application.seller.manager.SellerCsReadManager;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;

@Component
public class SellerCsValidator {

    private final SellerCsReadManager sellerCsReadManager;

    public SellerCsValidator(SellerCsReadManager sellerCsReadManager) {
        this.sellerCsReadManager = sellerCsReadManager;
    }

    public SellerCs findExistingOrThrow(SellerId sellerId) {
        return sellerCsReadManager.getBySellerId(sellerId);
    }
}
