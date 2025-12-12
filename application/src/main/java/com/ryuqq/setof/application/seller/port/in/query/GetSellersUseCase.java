package com.ryuqq.setof.application.seller.port.in.query;

import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;

/**
 * Get Sellers UseCase (Query)
 *
 * <p>셀러 목록 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetSellersUseCase {

    /**
     * 검색 조건으로 셀러 목록 조회
     *
     * @param query 검색 조건
     * @return 셀러 목록 페이지
     */
    PageResponse<SellerSummaryResponse> execute(SellerSearchQuery query);
}
