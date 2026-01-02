package com.ryuqq.setof.application.faqcategory.factory.query;

import com.ryuqq.setof.application.faqcategory.dto.query.SearchFaqCategoryQuery;
import com.ryuqq.setof.domain.faq.query.FaqCategorySearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryStatus;
import org.springframework.stereotype.Component;

/**
 * FAQ 카테고리 Query Factory
 *
 * <p>FAQ 카테고리 검색 조건 생성을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqCategoryQueryFactory {

    /**
     * 검색 조건 생성
     *
     * @param query 검색 쿼리
     * @return 검색 조건
     */
    public FaqCategorySearchCriteria createSearchCriteria(SearchFaqCategoryQuery query) {
        FaqCategoryStatus status = parseStatus(query.status());
        long offset = (long) query.page() * query.size();

        return new FaqCategorySearchCriteria(status, offset, query.size());
    }

    private FaqCategoryStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return FaqCategoryStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
