package com.ryuqq.setof.application.category.port.in.query;

import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import java.util.List;

/**
 * 자식 카테고리 조회 UseCase.
 *
 * <p>특정 카테고리의 직계 자식 카테고리들을 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface GetChildCategoriesUseCase {

    /**
     * 특정 카테고리의 자식 카테고리를 조회합니다.
     *
     * @param categoryId 부모 카테고리 ID
     * @return 자식 카테고리 목록 (트리 구조)
     */
    List<TreeCategoryResult> execute(Long categoryId);
}
