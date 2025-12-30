package com.ryuqq.setof.adapter.in.rest.v2.faqcategory.mapper;

import com.ryuqq.setof.application.faqcategory.dto.query.SearchFaqCategoryQuery;
import org.springframework.stereotype.Component;

/**
 * FAQ Category V2 API Mapper
 *
 * <p>API Request DTO → Application Query 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class FaqCategoryV2ApiMapper {

    /**
     * Client용 SearchQuery 생성
     *
     * <p>ACTIVE 상태의 FAQ 카테고리만 조회
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return Application Query
     */
    public SearchFaqCategoryQuery toSearchQuery(int page, int size) {
        return SearchFaqCategoryQuery.forClient(page, size);
    }
}
