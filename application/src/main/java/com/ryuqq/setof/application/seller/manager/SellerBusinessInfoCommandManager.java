package com.ryuqq.setof.application.seller.manager;

import com.ryuqq.setof.application.seller.port.out.command.SellerBusinessInfoCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SellerBusinessInfoCommandManager {

    private final SellerBusinessInfoCommandPort sellerBusinessInfoCommandPort;

    public SellerBusinessInfoCommandManager(
            SellerBusinessInfoCommandPort sellerBusinessInfoCommandPort) {
        this.sellerBusinessInfoCommandPort = sellerBusinessInfoCommandPort;
    }

    public SellerBusinessInfo persist(SellerBusinessInfo businessInfo) {
        return sellerBusinessInfoCommandPort.persist(businessInfo);
    }

    public List<SellerBusinessInfo> persistAll(List<SellerBusinessInfo> businessInfos) {
        return sellerBusinessInfoCommandPort.persistAll(businessInfos);
    }
}
