package com.ryuqq.setof.application.seller.port.out.query;

import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.id.SellerBusinessInfoId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.Optional;

/** SellerBusinessInfo Query Port. */
public interface SellerBusinessInfoQueryPort {

    Optional<SellerBusinessInfo> findById(SellerBusinessInfoId id);

    Optional<SellerBusinessInfo> findBySellerId(SellerId sellerId);

    boolean existsBySellerId(SellerId sellerId);

    boolean existsByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumberExcluding(String registrationNumber, SellerId excludeId);
}
