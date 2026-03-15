package com.ryuqq.setof.domain.navigation.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;
import java.time.Instant;

/**
 * NavigationMenu 검색 조건 Criteria.
 *
 * @param displayPeriodStart 전시 기간 시작 필터 (nullable)
 * @param displayPeriodEnd 전시 기간 종료 필터 (nullable)
 * @param queryContext 정렬 및 페이징 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record NavigationMenuSearchCriteria(
        Instant displayPeriodStart,
        Instant displayPeriodEnd,
        QueryContext<NavigationMenuSortKey> queryContext) {

    public static NavigationMenuSearchCriteria of(
            Instant displayPeriodStart,
            Instant displayPeriodEnd,
            QueryContext<NavigationMenuSortKey> queryContext) {
        return new NavigationMenuSearchCriteria(displayPeriodStart, displayPeriodEnd, queryContext);
    }

    public int size() {
        return queryContext.size();
    }

    public long offset() {
        return queryContext.offset();
    }
}
