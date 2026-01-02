package com.ryuqq.setof.application.faq.port.in.query;

import com.ryuqq.setof.application.faq.dto.query.SearchFaqQuery;
import com.ryuqq.setof.application.faq.dto.response.FaqResponse;
import java.util.List;

/**
 * FAQ 검색 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SearchFaqUseCase {

    /**
     * FAQ 목록 검색
     *
     * @param query 검색 조건
     * @return FAQ 목록
     */
    List<FaqResponse> execute(SearchFaqQuery query);

    /**
     * FAQ 개수 조회
     *
     * @param query 검색 조건
     * @return FAQ 개수
     */
    long count(SearchFaqQuery query);
}
