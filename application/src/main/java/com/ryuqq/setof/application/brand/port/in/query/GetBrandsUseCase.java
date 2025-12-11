package com.ryuqq.setof.application.brand.port.in.query;

import com.ryuqq.setof.application.brand.dto.query.BrandSearchQuery;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.application.common.response.PageResponse;

/**
 * Get Brands UseCase (Query)
 *
 * <p>브랜드 목록을 페이징 조회하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetBrandsUseCase {

    /**
     * 브랜드 목록 조회 실행
     *
     * @param condition 검색 조건
     * @return 페이징된 브랜드 목록
     */
    PageResponse<BrandSummaryResponse> execute(BrandSearchQuery query);
}
