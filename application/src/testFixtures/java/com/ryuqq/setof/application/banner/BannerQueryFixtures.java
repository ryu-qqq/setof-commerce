package com.ryuqq.setof.application.banner;

import com.ryuqq.setof.application.banner.dto.query.BannerGroupPageResult;
import com.ryuqq.setof.application.banner.dto.query.BannerGroupSearchParams;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Banner Application Query 테스트 Fixtures.
 *
 * <p>BannerSlideQueryService, BannerSlideQueryManager, SearchBannerGroupsService,
 * GetBannerGroupDetailService, BannerGroupQueryFactory, BannerGroupReadManager 테스트에서 공통으로 사용하는 객체
 * 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BannerQueryFixtures {

    private BannerQueryFixtures() {}

    // ===== BannerType =====

    public static BannerType defaultBannerType() {
        return BannerType.RECOMMEND;
    }

    // ===== BannerSlide List =====

    public static List<BannerSlide> activeBannerSlides() {
        return List.of(BannerFixtures.activeBannerSlide(1L), BannerFixtures.activeBannerSlide(2L));
    }

    public static List<BannerSlide> emptyBannerSlides() {
        return List.of();
    }

    // ===== BannerGroupSearchParams =====

    public static BannerGroupSearchParams searchParams() {
        return new BannerGroupSearchParams(null, null, null, null, null, null, 0, 20);
    }

    public static BannerGroupSearchParams searchParams(int page, int size) {
        return new BannerGroupSearchParams(null, null, null, null, null, null, page, size);
    }

    public static BannerGroupSearchParams searchParamsWithBannerType(String bannerType) {
        return new BannerGroupSearchParams(bannerType, null, null, null, null, null, 0, 20);
    }

    public static BannerGroupSearchParams searchParamsWithInvalidBannerType() {
        return new BannerGroupSearchParams("INVALID_TYPE", null, null, null, null, null, 0, 20);
    }

    public static BannerGroupSearchParams searchParamsWithBlankBannerType() {
        return new BannerGroupSearchParams("", null, null, null, null, null, 0, 20);
    }

    public static BannerGroupSearchParams searchParamsActiveOnly() {
        return new BannerGroupSearchParams(null, true, null, null, null, null, 0, 20);
    }

    public static BannerGroupSearchParams searchParamsWithPeriod(
            Instant displayPeriodStart, Instant displayPeriodEnd) {
        return new BannerGroupSearchParams(
                null, null, displayPeriodStart, displayPeriodEnd, null, null, 0, 20);
    }

    public static BannerGroupSearchParams searchParamsWithTitleKeyword(String titleKeyword) {
        return new BannerGroupSearchParams(null, null, null, null, titleKeyword, null, 0, 20);
    }

    public static BannerGroupSearchParams searchParamsWithLastDomainId(Long lastDomainId) {
        return new BannerGroupSearchParams(null, null, null, null, null, lastDomainId, 0, 20);
    }

    // ===== BannerGroup List =====

    public static List<BannerGroup> activeBannerGroups() {
        return List.of(BannerFixtures.activeBannerGroup(1L), BannerFixtures.activeBannerGroup(2L));
    }

    public static List<BannerGroup> emptyBannerGroups() {
        return Collections.emptyList();
    }

    // ===== BannerGroupPageResult =====

    public static BannerGroupPageResult bannerGroupPageResult() {
        List<BannerGroup> groups = activeBannerGroups();
        return BannerGroupPageResult.of(groups, 2L, 0, 20, 2L);
    }

    public static BannerGroupPageResult emptyBannerGroupPageResult() {
        return BannerGroupPageResult.of(Collections.emptyList(), 0L, 0, 20, null);
    }

    public static BannerGroupPageResult bannerGroupPageResult(
            List<BannerGroup> groups, long totalCount, int page, int size) {
        Long lastDomainId = groups.isEmpty() ? null : groups.get(groups.size() - 1).idValue();
        return BannerGroupPageResult.of(groups, totalCount, page, size, lastDomainId);
    }
}
