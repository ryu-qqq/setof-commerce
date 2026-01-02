package com.ryuqq.setof.application.review.factory.query;

import com.ryuqq.setof.application.review.dto.query.ReviewSearchQuery;
import com.ryuqq.setof.domain.review.query.criteria.ReviewSearchCriteria;
import org.springframework.stereotype.Component;

/**
 * ReviewQueryFactory - 리뷰 검색 조건 Criteria 생성 팩토리
 *
 * <p>Application Layer의 Query DTO를 Domain Layer의 Criteria로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ReviewQueryFactory {

    /**
     * ReviewSearchQuery로부터 Criteria 생성
     *
     * @param query 리뷰 검색 Query DTO
     * @return ReviewSearchCriteria 인스턴스
     */
    public ReviewSearchCriteria create(ReviewSearchQuery query) {
        if (query.productGroupId() != null) {
            return ReviewSearchCriteria.ofProductGroup(
                    query.productGroupId(), query.page(), query.size());
        }
        if (query.memberId() != null) {
            return ReviewSearchCriteria.ofMember(query.memberId(), query.page(), query.size());
        }
        return ReviewSearchCriteria.ofProductGroup(null, query.page(), query.size());
    }
}
