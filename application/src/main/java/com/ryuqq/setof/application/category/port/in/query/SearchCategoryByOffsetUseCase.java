package com.ryuqq.setof.application.category.port.in.query;

import com.ryuqq.setof.application.category.dto.query.CategorySearchParams;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;

/**
 * 카테고리 검색 UseCase (Offset 기반 페이징).
 *
 * <p>APP-UC-001: Offset 페이징은 Search{Domain}ByOffsetUseCase.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface SearchCategoryByOffsetUseCase {

    /**
     * 카테고리를 검색합니다.
     *
     * @param params 검색 파라미터
     * @return 카테고리 페이지 결과
     */
    CategoryPageResult execute(CategorySearchParams params);
}
