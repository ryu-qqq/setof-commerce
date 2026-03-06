package com.ryuqq.setof.application.seller.port.out.query;

import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.Optional;

public interface SellerCsQueryPort {
    Optional<SellerCs> findById(Long id);

    Optional<SellerCs> findBySellerId(SellerId sellerId);

    boolean existsBySellerId(SellerId sellerId);
}
