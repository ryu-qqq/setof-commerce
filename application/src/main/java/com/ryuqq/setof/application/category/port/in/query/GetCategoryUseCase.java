package com.ryuqq.setof.application.category.port.in.query;

import com.ryuqq.setof.application.category.dto.response.CategoryResponse;

/**
 * Get Category UseCase (Query)
 *
 * <p>카테고리 단건 조회하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCategoryUseCase {

    /**
     * 카테고리 ID로 단건 조회
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 상세 정보
     */
    CategoryResponse getCategory(Long categoryId);
}
