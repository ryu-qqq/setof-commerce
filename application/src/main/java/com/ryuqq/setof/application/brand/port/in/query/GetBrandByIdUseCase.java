package com.ryuqq.setof.application.brand.port.in.query;

import com.ryuqq.setof.application.brand.dto.response.BrandResult;

/**
 * 브랜드 단건 조회 UseCase.
 *
 * <p>브랜드 ID로 단건 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface GetBrandByIdUseCase {

    /**
     * 브랜드를 조회합니다.
     *
     * @param brandId 브랜드 ID
     * @return 브랜드 결과
     */
    BrandResult execute(Long brandId);
}
