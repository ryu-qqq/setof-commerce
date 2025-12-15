package com.ryuqq.setof.application.seller.port.in.query;

import com.ryuqq.setof.application.seller.dto.response.SellerResponse;

/**
 * Get Seller UseCase (Query)
 *
 * <p>셀러 단건 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetSellerUseCase {

    /**
     * 셀러 ID로 단건 조회
     *
     * @param sellerId 셀러 ID
     * @return 셀러 상세 정보
     */
    SellerResponse execute(Long sellerId);
}
