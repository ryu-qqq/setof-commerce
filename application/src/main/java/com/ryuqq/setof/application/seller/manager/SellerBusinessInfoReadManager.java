package com.ryuqq.setof.application.seller.manager;

import com.ryuqq.setof.application.seller.port.out.query.SellerBusinessInfoQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.exception.BusinessInfoNotFoundException;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;

@Component
public class SellerBusinessInfoReadManager {

    private final SellerBusinessInfoQueryPort sellerBusinessInfoQueryPort;

    public SellerBusinessInfoReadManager(SellerBusinessInfoQueryPort sellerBusinessInfoQueryPort) {
        this.sellerBusinessInfoQueryPort = sellerBusinessInfoQueryPort;
    }

    public SellerBusinessInfo getBySellerId(SellerId sellerId) {
        return sellerBusinessInfoQueryPort
                .findBySellerId(sellerId)
                .orElseThrow(BusinessInfoNotFoundException::new);
    }

    public boolean existsBySellerId(SellerId sellerId) {
        return sellerBusinessInfoQueryPort.existsBySellerId(sellerId);
    }

    public boolean existsByRegistrationNumber(String registrationNumber) {
        return sellerBusinessInfoQueryPort.existsByRegistrationNumber(registrationNumber);
    }

    public boolean existsByRegistrationNumberExcluding(
            String registrationNumber, SellerId excludeId) {
        return sellerBusinessInfoQueryPort.existsByRegistrationNumberExcluding(
                registrationNumber, excludeId);
    }
}
