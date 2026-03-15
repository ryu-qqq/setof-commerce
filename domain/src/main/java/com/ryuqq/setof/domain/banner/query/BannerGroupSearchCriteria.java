package com.ryuqq.setof.domain.banner.query;

import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import java.time.Instant;

/**
 * BannerGroup 검색 조건 Criteria.
 *
 * @param bannerType 배너 타입 필터 (nullable)
 * @param active 활성 여부 필터 (nullable, null이면 전체)
 * @param displayPeriodStart 전시 기간 시작 필터 (nullable)
 * @param displayPeriodEnd 전시 기간 종료 필터 (nullable)
 * @param titleKeyword 제목 검색어 (nullable)
 * @param lastDomainId No-Offset 페이징용 마지막 ID (nullable)
 * @param queryContext 정렬 및 페이징 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BannerGroupSearchCriteria(
        BannerType bannerType,
        Boolean active,
        Instant displayPeriodStart,
        Instant displayPeriodEnd,
        String titleKeyword,
        Long lastDomainId,
        QueryContext<BannerGroupSortKey> queryContext) {

    public static BannerGroupSearchCriteria of(
            BannerType bannerType,
            Boolean active,
            Instant displayPeriodStart,
            Instant displayPeriodEnd,
            String titleKeyword,
            Long lastDomainId,
            QueryContext<BannerGroupSortKey> queryContext) {
        return new BannerGroupSearchCriteria(
                bannerType,
                active,
                displayPeriodStart,
                displayPeriodEnd,
                titleKeyword,
                lastDomainId,
                queryContext);
    }

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
