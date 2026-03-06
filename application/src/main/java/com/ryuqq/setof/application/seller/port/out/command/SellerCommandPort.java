package com.ryuqq.setof.application.seller.port.out.command;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import java.util.List;

public interface SellerCommandPort {
    Seller persist(Seller seller);

    List<Seller> persistAll(List<Seller> sellers);
}
