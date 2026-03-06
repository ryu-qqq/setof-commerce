package com.ryuqq.setof.application.seller.manager;

import com.ryuqq.setof.application.seller.port.out.command.SellerCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SellerCommandManager {

    private final SellerCommandPort sellerCommandPort;

    public SellerCommandManager(SellerCommandPort sellerCommandPort) {
        this.sellerCommandPort = sellerCommandPort;
    }

    public Seller persist(Seller seller) {
        return sellerCommandPort.persist(seller);
    }

    public List<Seller> persistAll(List<Seller> sellers) {
        return sellerCommandPort.persistAll(sellers);
    }
}
