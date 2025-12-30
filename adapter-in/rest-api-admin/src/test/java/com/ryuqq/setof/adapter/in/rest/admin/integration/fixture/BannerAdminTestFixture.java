package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.CreateBannerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerV2ApiRequest;
import com.ryuqq.setof.application.banner.dto.response.BannerResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Banner Admin 통합 테스트용 Fixture
 *
 * @author development-team
 * @since 2.0.0
 */
public final class BannerAdminTestFixture {

    private BannerAdminTestFixture() {}

    // ===== IDs =====
    public static final Long BANNER_ID_1 = 1L;
    public static final Long BANNER_ID_2 = 2L;
    public static final Long NON_EXISTENT_BANNER_ID = 99999L;

    // ===== Banner 속성 =====
    public static final String BANNER_TITLE = "메인 배너";
    public static final String BANNER_TYPE_CATEGORY = "CATEGORY";
    public static final String BANNER_TYPE_PROMOTION = "PROMOTION";
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";

    // ===== Display Date =====
    public static final Instant DISPLAY_START_DATE = Instant.now().minus(1, ChronoUnit.DAYS);
    public static final Instant DISPLAY_END_DATE = Instant.now().plus(30, ChronoUnit.DAYS);

    /** 배너 생성 요청 생성 */
    public static CreateBannerV2ApiRequest createBannerRequest() {
        return new CreateBannerV2ApiRequest(
                BANNER_TITLE, BANNER_TYPE_CATEGORY, DISPLAY_START_DATE, DISPLAY_END_DATE);
    }

    /** 배너 생성 요청 생성 (커스텀) */
    public static CreateBannerV2ApiRequest createBannerRequest(String title, String type) {
        return new CreateBannerV2ApiRequest(title, type, DISPLAY_START_DATE, DISPLAY_END_DATE);
    }

    /** 배너 수정 요청 생성 */
    public static UpdateBannerV2ApiRequest updateBannerRequest() {
        return new UpdateBannerV2ApiRequest(
                BANNER_TITLE + " (수정)",
                DISPLAY_START_DATE,
                DISPLAY_END_DATE.plus(30, ChronoUnit.DAYS));
    }

    /** 배너 응답 생성 (Mock용) */
    public static BannerResponse createBannerResponse(Long bannerId) {
        return BannerResponse.of(
                bannerId,
                BANNER_TITLE,
                BANNER_TYPE_CATEGORY,
                STATUS_ACTIVE,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                Instant.now().minus(7, ChronoUnit.DAYS),
                Instant.now());
    }

    /** 배너 응답 생성 (커스텀) */
    public static BannerResponse createBannerResponse(
            Long bannerId, String title, String type, String status) {
        return BannerResponse.of(
                bannerId,
                title,
                type,
                status,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                Instant.now().minus(7, ChronoUnit.DAYS),
                Instant.now());
    }

    /** 배너 응답 목록 생성 (Mock용) */
    public static List<BannerResponse> createBannerResponses() {
        return List.of(
                createBannerResponse(BANNER_ID_1, "카테고리 배너", BANNER_TYPE_CATEGORY, STATUS_ACTIVE),
                createBannerResponse(BANNER_ID_2, "프로모션 배너", BANNER_TYPE_PROMOTION, STATUS_ACTIVE));
    }
}
