package com.ryuqq.setof.application.category.port.in.query;

import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import java.util.List;

/**
 * 부모 카테고리 조회 UseCase.
 *
 * <p>특정 카테고리의 모든 상위(부모) 카테고리들을 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface GetParentCategoriesUseCase {

    /**
     * 특정 카테고리의 부모 카테고리들을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 부모 카테고리 목록 (루트부터 순서대로)
     */
    List<TreeCategoryResult> execute(Long categoryId);
}
