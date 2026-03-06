package com.ryuqq.setof.application.seller.port.out.query;

import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.Optional;

public interface SellerBusinessInfoQueryPort {
    Optional<SellerBusinessInfo> findById(Long id);

    Optional<SellerBusinessInfo> findBySellerId(SellerId sellerId);

    boolean existsBySellerId(SellerId sellerId);

    boolean existsByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumberExcluding(String registrationNumber, SellerId excludeId);
}
