package com.ryuqq.setof.application.content.factory.query;

import com.ryuqq.setof.application.content.dto.query.SearchContentQuery;
import com.ryuqq.setof.domain.cms.query.criteria.ContentSearchCriteria;
import com.ryuqq.setof.domain.cms.vo.ContentStatus;
import org.springframework.stereotype.Component;

/**
 * Content Query Factory
 *
 * <p>Query DTO → Criteria 변환 전용 Factory
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ContentQueryFactory {

    /**
     * 검색 조건 Criteria 생성
     *
     * @param query 검색 Query DTO
     * @return ContentSearchCriteria
     */
    public ContentSearchCriteria create(SearchContentQuery query) {
        ContentStatus status = parseStatus(query.status());

        return ContentSearchCriteria.of(
                query.title(), status, query.displayableAt(), query.offset(), query.size());
    }

    private ContentStatus parseStatus(String statusString) {
        if (statusString == null || statusString.isBlank()) {
            return null;
        }
        try {
            return ContentStatus.valueOf(statusString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
