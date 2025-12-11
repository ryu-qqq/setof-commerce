package com.ryuqq.setof.application.brand.port.in.query;

import com.ryuqq.setof.application.brand.dto.response.BrandResponse;

/**
 * Get Brand UseCase (Query)
 *
 * <p>브랜드 단건 조회하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetBrandUseCase {

    /**
     * 브랜드 ID로 단건 조회
     *
     * @param brandId 브랜드 ID
     * @return 브랜드 상세 정보
     */
    BrandResponse execute(Long brandId);
}
