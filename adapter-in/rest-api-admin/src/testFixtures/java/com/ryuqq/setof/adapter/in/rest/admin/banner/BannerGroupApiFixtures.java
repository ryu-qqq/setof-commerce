package com.ryuqq.setof.adapter.in.rest.admin.banner;

import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.ChangeBannerGroupStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.RegisterBannerGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.RegisterBannerSlideApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerSlideApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerSlidesApiRequest;
import java.time.Instant;
import java.util.List;

/**
 * BannerGroup API 테스트 Fixtures.
 *
 * <p>배너 그룹 API 테스트에서 사용되는 Request 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BannerGroupApiFixtures {

    private BannerGroupApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_BANNER_GROUP_ID = 1L;
    public static final Long DEFAULT_SLIDE_ID = 10L;
    public static final String DEFAULT_TITLE = "메인 배너";
    public static final String DEFAULT_BANNER_TYPE = "MAIN_BANNER";
    public static final Instant DEFAULT_DISPLAY_START_AT = Instant.parse("2025-01-01T00:00:00Z");
    public static final Instant DEFAULT_DISPLAY_END_AT = Instant.parse("2025-12-31T23:59:59Z");
    public static final String DEFAULT_SLIDE_TITLE = "여름 이벤트 슬라이드";
    public static final String DEFAULT_IMAGE_URL = "https://cdn.example.com/banner/image.jpg";
    public static final String DEFAULT_LINK_URL = "/event/summer";

    // ===== RegisterBannerGroupApiRequest Fixtures =====

    public static RegisterBannerGroupApiRequest registerRequest() {
        return new RegisterBannerGroupApiRequest(
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                List.of(registerSlideRequest()));
    }

    public static RegisterBannerGroupApiRequest registerRequestWithMultipleSlides() {
        return new RegisterBannerGroupApiRequest(
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                List.of(
                        registerSlideRequest(),
                        new RegisterBannerSlideApiRequest(
                                "두 번째 슬라이드",
                                "https://cdn.example.com/banner/image2.jpg",
                                "/event/winter",
                                1,
                                DEFAULT_DISPLAY_START_AT,
                                DEFAULT_DISPLAY_END_AT,
                                true)));
    }

    public static RegisterBannerGroupApiRequest registerRequestInactive() {
        return new RegisterBannerGroupApiRequest(
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                List.of(registerSlideRequest()));
    }

    // ===== UpdateBannerGroupApiRequest Fixtures =====

    public static UpdateBannerGroupApiRequest updateRequest() {
        return new UpdateBannerGroupApiRequest(
                DEFAULT_TITLE + " (수정)",
                DEFAULT_BANNER_TYPE,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true);
    }

    public static UpdateBannerGroupApiRequest updateRequestInactive() {
        return new UpdateBannerGroupApiRequest(
                DEFAULT_TITLE + " (수정)",
                DEFAULT_BANNER_TYPE,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false);
    }

    // ===== UpdateBannerSlidesApiRequest Fixtures =====

    public static UpdateBannerSlidesApiRequest updateSlidesRequest() {
        return new UpdateBannerSlidesApiRequest(
                List.of(updateSlideRequestWithId(), updateSlideRequestWithNullId()));
    }

    public static UpdateBannerSlidesApiRequest updateSlidesRequestWithExistingSlideOnly() {
        return new UpdateBannerSlidesApiRequest(List.of(updateSlideRequestWithId()));
    }

    public static UpdateBannerSlidesApiRequest updateSlidesRequestWithNewSlideOnly() {
        return new UpdateBannerSlidesApiRequest(List.of(updateSlideRequestWithNullId()));
    }

    // ===== ChangeBannerGroupStatusApiRequest Fixtures =====

    public static ChangeBannerGroupStatusApiRequest changeStatusActiveRequest() {
        return new ChangeBannerGroupStatusApiRequest(true);
    }

    public static ChangeBannerGroupStatusApiRequest changeStatusInactiveRequest() {
        return new ChangeBannerGroupStatusApiRequest(false);
    }

    // ===== Inner Request Fixtures =====

    public static RegisterBannerSlideApiRequest registerSlideRequest() {
        return new RegisterBannerSlideApiRequest(
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                0,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true);
    }

    /** slideId 존재 - 기존 슬라이드 수정 케이스. */
    public static UpdateBannerSlideApiRequest updateSlideRequestWithId() {
        return new UpdateBannerSlideApiRequest(
                DEFAULT_SLIDE_ID,
                DEFAULT_SLIDE_TITLE + " (수정)",
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                0,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true);
    }

    /** slideId null - 신규 슬라이드 등록 케이스. */
    public static UpdateBannerSlideApiRequest updateSlideRequestWithNullId() {
        return new UpdateBannerSlideApiRequest(
                null,
                "신규 슬라이드",
                "https://cdn.example.com/banner/new-image.jpg",
                "/event/new",
                1,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true);
    }
}
