package com.ryuqq.setof.application.seller.port.out.query;

import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.Optional;

/** SellerAddress Query Port. */
public interface SellerAddressQueryPort {

    Optional<SellerAddress> findById(SellerAddressId id);

    Optional<SellerAddress> findBySellerId(SellerId sellerId);

    boolean existsBySellerId(SellerId sellerId);
}
