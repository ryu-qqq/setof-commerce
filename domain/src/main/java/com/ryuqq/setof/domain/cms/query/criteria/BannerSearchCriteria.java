package com.ryuqq.setof.domain.cms.query.criteria;

import com.ryuqq.setof.domain.cms.vo.BannerStatus;
import com.ryuqq.setof.domain.cms.vo.BannerType;
import java.time.Instant;

/**
 * Banner 검색 조건 Criteria
 *
 * @param bannerType 배너 타입 (nullable)
 * @param status 상태 (nullable)
 * @param displayableAt 노출 가능 시점 (nullable)
 * @param offset 페이징 오프셋
 * @param limit 페이징 크기
 * @author development-team
 * @since 1.0.0
 */
public record BannerSearchCriteria(
        BannerType bannerType, BannerStatus status, Instant displayableAt, int offset, int limit) {

    /** 정적 팩토리 메서드 */
    public static BannerSearchCriteria of(
            BannerType bannerType,
            BannerStatus status,
            Instant displayableAt,
            int offset,
            int limit) {
        return new BannerSearchCriteria(bannerType, status, displayableAt, offset, limit);
    }

    /** 타입 조건 존재 여부 */
    public boolean hasBannerType() {
        return bannerType != null;
    }

    /** 상태 조건 존재 여부 */
    public boolean hasStatus() {
        return status != null;
    }

    /** 노출 시점 조건 존재 여부 */
    public boolean hasDisplayableAt() {
        return displayableAt != null;
    }
}
