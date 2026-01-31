package com.ryuqq.setof.application.seller.manager;

import com.ryuqq.setof.application.seller.port.out.query.SellerAddressQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.exception.AddressNotFoundException;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** SellerAddress Read Manager. */
@Component
public class SellerAddressReadManager {

    private final SellerAddressQueryPort queryPort;

    public SellerAddressReadManager(SellerAddressQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public SellerAddress getById(SellerAddressId id) {
        return queryPort.findById(id).orElseThrow(AddressNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public SellerAddress getBySellerId(SellerId sellerId) {
        return queryPort.findBySellerId(sellerId).orElseThrow(AddressNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public boolean existsBySellerId(SellerId sellerId) {
        return queryPort.existsBySellerId(sellerId);
    }
}
