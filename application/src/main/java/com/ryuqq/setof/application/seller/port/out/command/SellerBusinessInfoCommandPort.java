package com.ryuqq.setof.application.seller.port.out.command;

import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import java.util.List;

/** SellerBusinessInfo Command Port. */
public interface SellerBusinessInfoCommandPort {

    Long persist(SellerBusinessInfo businessInfo);

    void persistAll(List<SellerBusinessInfo> businessInfos);
}
