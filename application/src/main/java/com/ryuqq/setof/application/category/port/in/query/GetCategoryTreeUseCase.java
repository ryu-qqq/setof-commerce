package com.ryuqq.setof.application.category.port.in.query;

import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import java.util.List;

/**
 * Get Category Tree UseCase (Query)
 *
 * <p>전체 카테고리 트리를 조회하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCategoryTreeUseCase {

    /**
     * 전체 카테고리 트리 조회
     *
     * @return 카테고리 트리 목록 (최상위부터)
     */
    List<CategoryTreeResponse> getCategoryTree();
}
