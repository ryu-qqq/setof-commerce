package com.ryuqq.setof.application.content.port.in.query;

import com.ryuqq.setof.application.content.dto.query.SearchContentQuery;
import com.ryuqq.setof.application.content.dto.response.ContentSummaryResponse;
import java.util.List;

/**
 * Content 목록 조회 UseCase (Query)
 *
 * <p>콘텐츠 목록 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SearchContentUseCase {

    /**
     * 콘텐츠 목록 조회
     *
     * @param query 검색 조건
     * @return 콘텐츠 요약 목록
     */
    List<ContentSummaryResponse> execute(SearchContentQuery query);

    /**
     * 콘텐츠 총 개수 조회
     *
     * @param query 검색 조건
     * @return 총 개수
     */
    long count(SearchContentQuery query);
}
