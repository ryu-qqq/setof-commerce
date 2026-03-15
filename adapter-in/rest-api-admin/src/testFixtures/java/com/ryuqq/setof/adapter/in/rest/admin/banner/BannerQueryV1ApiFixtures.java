package com.ryuqq.setof.adapter.in.rest.admin.banner;

import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SearchBannersV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.BannerGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.BannerItemV1ApiResponse;
import com.ryuqq.setof.application.banner.dto.query.BannerGroupPageResult;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.ryuqq.setof.domain.banner.id.BannerSlideId;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Banner Query v1 API 테스트 Fixtures.
 *
 * <p>배너 조회 API 테스트에서 사용되는 Request/Response/Domain 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BannerQueryV1ApiFixtures {

    private BannerQueryV1ApiFixtures() {}

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    // ===== 기본 상수 =====
    public static final Long DEFAULT_BANNER_GROUP_ID = 1L;
    public static final Long DEFAULT_BANNER_SLIDE_ID = 10L;
    public static final String DEFAULT_BANNER_TITLE = "메인 배너";
    public static final String DEFAULT_SLIDE_TITLE = "여름 이벤트 슬라이드";
    public static final String DEFAULT_IMAGE_URL = "https://cdn.example.com/banner/image.jpg";
    public static final String DEFAULT_LINK_URL = "/event/summer";
    public static final BannerType DEFAULT_BANNER_TYPE = BannerType.CATEGORY;

    // UTC 기준 Instant (KST 2025-01-01 09:00:00 = UTC 2025-01-01 00:00:00)
    public static final Instant DEFAULT_DISPLAY_START_AT = Instant.parse("2025-01-01T00:00:00Z");
    public static final Instant DEFAULT_DISPLAY_END_AT = Instant.parse("2025-12-31T14:59:59Z");
    public static final Instant DEFAULT_CREATED_AT = Instant.parse("2025-01-01T01:30:00Z");
    public static final Instant DEFAULT_UPDATED_AT = Instant.parse("2025-01-01T01:30:00Z");

    // KST LocalDateTime (Instant → KST 변환 결과)
    public static final LocalDateTime DEFAULT_DISPLAY_START_KST =
            LocalDateTime.of(2025, 1, 1, 9, 0, 0);
    public static final LocalDateTime DEFAULT_DISPLAY_END_KST =
            LocalDateTime.of(2025, 12, 31, 23, 59, 59);
    public static final LocalDateTime DEFAULT_CREATED_KST =
            LocalDateTime.of(2025, 1, 1, 10, 30, 0);

    // ===== Search Request Fixtures =====

    public static SearchBannersV1ApiRequest searchRequest() {
        return new SearchBannersV1ApiRequest(null, null, null, null, null, null, null, 0, 20);
    }

    public static SearchBannersV1ApiRequest searchRequestWithDisplayYn(String displayYn) {
        return new SearchBannersV1ApiRequest(
                null, displayYn, null, null, null, null, null, 0, 20);
    }

    public static SearchBannersV1ApiRequest searchRequestWithBannerType(String bannerType) {
        return new SearchBannersV1ApiRequest(
                bannerType, null, null, null, null, null, null, 0, 20);
    }

    public static SearchBannersV1ApiRequest searchRequestWithBannerName(String searchWord) {
        return new SearchBannersV1ApiRequest(
                null, null, null, null, null, "BANNER_NAME", searchWord, 0, 20);
    }

    public static SearchBannersV1ApiRequest searchRequestWithOperator(String keyword) {
        return new SearchBannersV1ApiRequest(
                null, null, null, null, null, keyword, "admin", 0, 20);
    }

    public static SearchBannersV1ApiRequest searchRequestWithNullPage() {
        return new SearchBannersV1ApiRequest(
                null, null, null, null, null, null, null, null, null);
    }

    public static SearchBannersV1ApiRequest searchRequestWithDateRange() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        return new SearchBannersV1ApiRequest(null, null, null, start, end, null, null, 0, 20);
    }

    // ===== Domain Object Fixtures =====

    public static BannerGroup bannerGroup() {
        return bannerGroup(DEFAULT_BANNER_GROUP_ID);
    }

    public static BannerGroup bannerGroup(Long id) {
        return bannerGroup(id, true);
    }

    public static BannerGroup bannerGroup(Long id, boolean active) {
        DisplayPeriod period = DisplayPeriod.of(DEFAULT_DISPLAY_START_AT, DEFAULT_DISPLAY_END_AT);
        List<BannerSlide> slides = List.of(bannerSlide(DEFAULT_BANNER_SLIDE_ID));
        return BannerGroup.reconstitute(
                BannerGroupId.of(id),
                DEFAULT_BANNER_TITLE,
                DEFAULT_BANNER_TYPE,
                period,
                active,
                DeletionStatus.active(),
                slides,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    public static BannerGroup bannerGroupWithSlides(Long id, List<BannerSlide> slides) {
        DisplayPeriod period = DisplayPeriod.of(DEFAULT_DISPLAY_START_AT, DEFAULT_DISPLAY_END_AT);
        return BannerGroup.reconstitute(
                BannerGroupId.of(id),
                DEFAULT_BANNER_TITLE,
                DEFAULT_BANNER_TYPE,
                period,
                true,
                DeletionStatus.active(),
                slides,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    public static BannerSlide bannerSlide(Long id) {
        return bannerSlide(id, true);
    }

    public static BannerSlide bannerSlide(Long id, boolean active) {
        DisplayPeriod period = DisplayPeriod.of(DEFAULT_DISPLAY_START_AT, DEFAULT_DISPLAY_END_AT);
        return BannerSlide.reconstitute(
                BannerSlideId.of(id),
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                1,
                period,
                active,
                DeletionStatus.active(),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    // ===== Application Result Fixtures =====

    public static BannerGroupPageResult bannerGroupPageResult() {
        return bannerGroupPageResult(List.of(bannerGroup()), 1L, 0, 20);
    }

    public static BannerGroupPageResult bannerGroupPageResult(
            List<BannerGroup> items, long totalCount, int page, int size) {
        return BannerGroupPageResult.of(items, totalCount, page, size, null);
    }

    public static BannerGroupPageResult emptyBannerGroupPageResult() {
        return BannerGroupPageResult.of(List.of(), 0L, 0, 20, null);
    }

    public static List<BannerGroup> multipleBannerGroups() {
        return List.of(bannerGroup(1L, true), bannerGroup(2L, true), bannerGroup(3L, false));
    }

    // ===== API Response Fixtures =====

    public static BannerGroupV1ApiResponse bannerGroupResponse() {
        return bannerGroupResponse(DEFAULT_BANNER_GROUP_ID);
    }

    public static BannerGroupV1ApiResponse bannerGroupResponse(Long id) {
        return BannerGroupV1ApiResponse.of(
                id,
                DEFAULT_BANNER_TITLE,
                DEFAULT_BANNER_TYPE.name(),
                BannerGroupV1ApiResponse.DisplayPeriodResponse.of(
                        DEFAULT_DISPLAY_START_KST, DEFAULT_DISPLAY_END_KST),
                "Y",
                DEFAULT_CREATED_KST,
                DEFAULT_CREATED_KST,
                "",
                "");
    }

    public static BannerItemV1ApiResponse bannerItemResponse() {
        return bannerItemResponse(DEFAULT_BANNER_SLIDE_ID);
    }

    public static BannerItemV1ApiResponse bannerItemResponse(Long slideId) {
        return BannerItemV1ApiResponse.of(
                slideId,
                DEFAULT_BANNER_TYPE.name(),
                DEFAULT_SLIDE_TITLE,
                BannerItemV1ApiResponse.DisplayPeriodResponse.of(
                        DEFAULT_DISPLAY_START_KST, DEFAULT_DISPLAY_END_KST),
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                1,
                "Y",
                BannerItemV1ApiResponse.ImageSizeResponse.of(0.0, 0.0));
    }

    // ===== 헬퍼 메서드 =====

    public static Instant toKstInstant(LocalDateTime ldt) {
        return ldt.atZone(KST).toInstant();
    }

    public static LocalDateTime toKstLocalDateTime(Instant instant) {
        return instant.atZone(KST).toLocalDateTime();
    }
}
