package com.ryuqq.setof.application.seller.port.in.query;

import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;

/** 어드민용 셀러 상세 조회 UseCase. */
public interface GetSellerForAdminUseCase {

    SellerFullCompositeResult execute(Long sellerId);
}
