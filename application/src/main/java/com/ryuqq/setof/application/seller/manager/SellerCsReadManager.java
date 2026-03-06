package com.ryuqq.setof.application.seller.manager;

import com.ryuqq.setof.application.seller.port.out.query.SellerCsQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.exception.SellerCsNotFoundException;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;

@Component
public class SellerCsReadManager {

    private final SellerCsQueryPort sellerCsQueryPort;

    public SellerCsReadManager(SellerCsQueryPort sellerCsQueryPort) {
        this.sellerCsQueryPort = sellerCsQueryPort;
    }

    public SellerCs getBySellerId(SellerId sellerId) {
        return sellerCsQueryPort
                .findBySellerId(sellerId)
                .orElseThrow(() -> new SellerCsNotFoundException(sellerId.value()));
    }

    public boolean existsBySellerId(SellerId sellerId) {
        return sellerCsQueryPort.existsBySellerId(sellerId);
    }
}
