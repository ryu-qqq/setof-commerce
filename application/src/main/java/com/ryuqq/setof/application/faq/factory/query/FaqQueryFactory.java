package com.ryuqq.setof.application.faq.factory.query;

import com.ryuqq.setof.application.faq.dto.query.SearchFaqQuery;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqStatus;
import org.springframework.stereotype.Component;

/**
 * FAQ Query Factory
 *
 * <p>Query DTO를 Domain 검색 조건으로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqQueryFactory {

    /**
     * SearchFaqQuery → FaqSearchCriteria 변환
     *
     * @param query 검색 쿼리
     * @return FaqSearchCriteria
     */
    public FaqSearchCriteria createSearchCriteria(SearchFaqQuery query) {
        FaqCategoryCode categoryCode =
                query.categoryCode() != null ? FaqCategoryCode.of(query.categoryCode()) : null;
        FaqStatus status = query.status() != null ? FaqStatus.valueOf(query.status()) : null;
        long offset = (long) query.page() * query.size();

        return new FaqSearchCriteria(categoryCode, status, query.isTop(), offset, query.size());
    }
}
