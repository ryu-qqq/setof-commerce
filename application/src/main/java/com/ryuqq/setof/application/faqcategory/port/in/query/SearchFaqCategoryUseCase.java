package com.ryuqq.setof.application.faqcategory.port.in.query;

import com.ryuqq.setof.application.faqcategory.dto.query.SearchFaqCategoryQuery;
import com.ryuqq.setof.application.faqcategory.dto.response.FaqCategoryResponse;
import java.util.List;

/**
 * FAQ 카테고리 검색 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SearchFaqCategoryUseCase {

    /**
     * FAQ 카테고리 목록 검색
     *
     * @param query 검색 조건
     * @return FAQ 카테고리 목록
     */
    List<FaqCategoryResponse> execute(SearchFaqCategoryQuery query);

    /**
     * FAQ 카테고리 개수 조회
     *
     * @param query 검색 조건
     * @return FAQ 카테고리 개수
     */
    long count(SearchFaqCategoryQuery query);
}
