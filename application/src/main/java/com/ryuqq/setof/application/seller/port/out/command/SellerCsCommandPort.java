package com.ryuqq.setof.application.seller.port.out.command;

import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import java.util.List;

public interface SellerCsCommandPort {
    SellerCs persist(SellerCs sellerCs);

    List<SellerCs> persistAll(List<SellerCs> sellerCsList);
}
