package com.ryuqq.setof.application.banner.dto.query;

import java.time.Instant;

/**
 * Banner 검색 Query
 *
 * @param bannerType 배너 타입 (nullable)
 * @param status 상태 (nullable)
 * @param displayableAt 노출 가능 시점 (nullable)
 * @param offset 페이징 오프셋
 * @param limit 페이징 크기
 * @author development-team
 * @since 1.0.0
 */
public record SearchBannerQuery(
        String bannerType, String status, Instant displayableAt, int offset, int limit) {

    /** 기본값 적용 */
    public SearchBannerQuery {
        if (limit <= 0) {
            limit = 20;
        }
        if (offset < 0) {
            offset = 0;
        }
    }

    /** 기본 검색 조건 */
    public static SearchBannerQuery defaultQuery() {
        return new SearchBannerQuery(null, null, null, 0, 20);
    }

    /** 타입 조건이 있는지 확인 */
    public boolean hasBannerType() {
        return bannerType != null && !bannerType.isBlank();
    }

    /** 상태 조건이 있는지 확인 */
    public boolean hasStatus() {
        return status != null && !status.isBlank();
    }
}
