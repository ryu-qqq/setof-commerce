package com.ryuqq.setof.application.category.port.in.query;

import com.ryuqq.setof.application.category.dto.response.CategoryResponse;
import java.util.List;

/**
 * Get Categories UseCase (Query)
 *
 * <p>하위 카테고리 목록을 조회하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCategoriesUseCase {

    /**
     * 하위 카테고리 목록 조회
     *
     * @param parentId 부모 카테고리 ID (null이면 최상위)
     * @return 카테고리 목록
     */
    List<CategoryResponse> getCategories(Long parentId);
}
