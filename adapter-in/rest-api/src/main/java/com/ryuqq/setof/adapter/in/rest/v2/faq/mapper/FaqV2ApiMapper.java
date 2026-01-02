package com.ryuqq.setof.adapter.in.rest.v2.faq.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.faq.dto.query.SearchFaqV2ApiRequest;
import com.ryuqq.setof.application.faq.dto.query.SearchFaqQuery;
import org.springframework.stereotype.Component;

/**
 * FAQ V2 API Mapper
 *
 * <p>API Request DTO → Application Query 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class FaqV2ApiMapper {

    /**
     * SearchRequest → SearchQuery 변환 (Client용)
     *
     * <p>PUBLISHED 상태의 FAQ만 조회
     *
     * @param request API 요청
     * @return Application Query
     */
    public SearchFaqQuery toSearchQuery(SearchFaqV2ApiRequest request) {
        return SearchFaqQuery.forClient(
                request.categoryCode(),
                null,
                request.keyword(),
                request.getPage(),
                request.getSize());
    }

    /**
     * 상단 FAQ 조회용 Query 생성
     *
     * @param categoryCode 카테고리 코드 (optional)
     * @param limit 조회 개수
     * @return Application Query
     */
    public SearchFaqQuery toTopQuery(String categoryCode, int limit) {
        return SearchFaqQuery.forClient(categoryCode, true, null, 0, limit);
    }
}
