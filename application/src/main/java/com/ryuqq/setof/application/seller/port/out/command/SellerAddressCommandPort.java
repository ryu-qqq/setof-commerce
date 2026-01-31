package com.ryuqq.setof.application.seller.port.out.command;

import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import java.util.List;

/** SellerAddress Command Port. */
public interface SellerAddressCommandPort {

    Long persist(SellerAddress address);

    void persistAll(List<SellerAddress> addresses);
}
