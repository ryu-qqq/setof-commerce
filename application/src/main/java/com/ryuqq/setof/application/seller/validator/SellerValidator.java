package com.ryuqq.setof.application.seller.validator;

import com.ryuqq.setof.application.seller.manager.SellerReadManager;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.exception.SellerNameDuplicateException;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;

@Component
public class SellerValidator {

    private final SellerReadManager sellerReadManager;

    public SellerValidator(SellerReadManager sellerReadManager) {
        this.sellerReadManager = sellerReadManager;
    }

    public Seller findExistingOrThrow(SellerId sellerId) {
        return sellerReadManager.getById(sellerId.value());
    }

    public void validateSellerNameNotDuplicate(String sellerName) {
        if (sellerReadManager.existsBySellerName(sellerName)) {
            throw new SellerNameDuplicateException(sellerName);
        }
    }

    public void validateSellerNameNotDuplicateExcluding(String sellerName, SellerId excludeId) {
        if (sellerReadManager.existsBySellerNameExcluding(sellerName, excludeId)) {
            throw new SellerNameDuplicateException(sellerName);
        }
    }
}
