package com.ryuqq.setof.domain.contentpage.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;
import java.time.Instant;

/**
 * ContentPage 목록 검색 조건 Criteria.
 *
 * @param active 활성 여부 필터 (nullable, null이면 전체)
 * @param displayPeriodStart 전시 기간 시작 필터 (nullable)
 * @param displayPeriodEnd 전시 기간 종료 필터 (nullable)
 * @param createdAtStart 등록일 시작 필터 (nullable)
 * @param createdAtEnd 등록일 종료 필터 (nullable)
 * @param titleKeyword 제목 검색어 (nullable)
 * @param contentPageId ID 검색 (nullable)
 * @param lastDomainId No-Offset 페이징용 마지막 ID (nullable)
 * @param queryContext 정렬 및 페이징 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ContentPageListSearchCriteria(
        Boolean active,
        Instant displayPeriodStart,
        Instant displayPeriodEnd,
        Instant createdAtStart,
        Instant createdAtEnd,
        String titleKeyword,
        Long contentPageId,
        Long lastDomainId,
        QueryContext<ContentPageSortKey> queryContext) {

    public int size() {
        return queryContext.size();
    }

    public long offset() {
        return queryContext.offset();
    }

    public int page() {
        return queryContext.page();
    }

    public boolean isNoOffset() {
        return lastDomainId != null;
    }
}
