package com.ryuqq.setof.application.seller.port.in.query;

import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;

public interface GetSellerForAdminUseCase {
    SellerCompositeResult execute(Long sellerId);
}
