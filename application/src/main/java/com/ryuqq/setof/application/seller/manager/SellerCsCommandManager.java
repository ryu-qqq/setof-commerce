package com.ryuqq.setof.application.seller.manager;

import com.ryuqq.setof.application.seller.port.out.command.SellerCsCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SellerCsCommandManager {

    private final SellerCsCommandPort sellerCsCommandPort;

    public SellerCsCommandManager(SellerCsCommandPort sellerCsCommandPort) {
        this.sellerCsCommandPort = sellerCsCommandPort;
    }

    public SellerCs persist(SellerCs sellerCs) {
        return sellerCsCommandPort.persist(sellerCs);
    }

    public List<SellerCs> persistAll(List<SellerCs> sellerCsList) {
        return sellerCsCommandPort.persistAll(sellerCsList);
    }
}
