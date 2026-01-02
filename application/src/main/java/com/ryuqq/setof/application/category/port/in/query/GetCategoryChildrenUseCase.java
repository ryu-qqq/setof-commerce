package com.ryuqq.setof.application.category.port.in.query;

import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import java.util.List;

/**
 * Get Category Children UseCase (Query)
 *
 * <p>특정 카테고리의 모든 하위 카테고리를 트리 구조로 조회하는 Inbound Port
 *
 * <p>Recursive CTE를 사용하여 모든 깊이의 하위 카테고리를 한 번에 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCategoryChildrenUseCase {

    /**
     * 특정 카테고리의 모든 하위 카테고리를 트리 구조로 조회
     *
     * @param categoryId 부모 카테고리 ID
     * @return 하위 카테고리 트리 목록
     */
    List<CategoryTreeResponse> getCategoryChildren(Long categoryId);
}
