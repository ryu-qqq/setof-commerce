package com.ryuqq.setof.application.seller.port.out.command;

import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import java.util.List;

public interface SellerBusinessInfoCommandPort {
    SellerBusinessInfo persist(SellerBusinessInfo businessInfo);

    List<SellerBusinessInfo> persistAll(List<SellerBusinessInfo> businessInfos);
}
