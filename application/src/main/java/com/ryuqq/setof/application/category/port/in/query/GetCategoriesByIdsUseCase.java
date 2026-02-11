package com.ryuqq.setof.application.category.port.in.query;

import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import java.util.List;

/**
 * 카테고리 ID 목록 조회 UseCase.
 *
 * <p>요청한 카테고리 ID 목록에 해당하는 카테고리들을 flat list로 조회합니다.
 *
 * <p>레거시 fetchAllParentCategoriesBulk (GET /category/parents?categoryIds=) 호환. API 명칭과 다르게 요청한 ID만
 * 조회 (상위 카테고리 재귀 탐색 없음).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface GetCategoriesByIdsUseCase {

    /**
     * 카테고리 ID 목록으로 카테고리를 조회합니다.
     *
     * @param categoryIds 카테고리 ID 목록
     * @return 조회된 카테고리 목록 (children 비어있음)
     */
    List<TreeCategoryResult> execute(List<Long> categoryIds);
}
