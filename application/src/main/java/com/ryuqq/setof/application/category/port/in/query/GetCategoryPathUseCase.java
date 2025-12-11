package com.ryuqq.setof.application.category.port.in.query;

import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse;

/**
 * Get Category Path UseCase (Query)
 *
 * <p>카테고리 상위 경로(breadcrumb)를 조회하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCategoryPathUseCase {

    /**
     * 카테고리 상위 경로 조회
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 경로 정보 (breadcrumb)
     */
    CategoryPathResponse getCategoryPath(Long categoryId);
}
