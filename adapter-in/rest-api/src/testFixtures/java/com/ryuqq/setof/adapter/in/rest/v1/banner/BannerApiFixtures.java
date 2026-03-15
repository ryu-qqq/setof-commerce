package com.ryuqq.setof.adapter.in.rest.v1.banner;

import com.ryuqq.setof.adapter.in.rest.v1.banner.dto.response.BannerSlideV1ApiResponse;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.id.BannerSlideId;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;
import java.util.List;

/**
 * Banner V1 API 테스트 Fixtures.
 *
 * <p>Banner 관련 Domain 객체 및 API Response 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BannerApiFixtures {

    private BannerApiFixtures() {}

    // ===== BannerSlide Domain Fixtures =====

    public static BannerSlide bannerSlide(long id) {
        return BannerSlide.reconstitute(
                BannerSlideId.of(id),
                "신규 회원 이벤트",
                "https://cdn.example.com/banner/1.jpg",
                "/event/new-member",
                1,
                DisplayPeriod.of(
                        Instant.parse("2000-01-01T00:00:00Z"),
                        Instant.parse("2099-12-31T23:59:59Z")),
                true,
                DeletionStatus.active(),
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-01T00:00:00Z"));
    }

    public static BannerSlide bannerSlide(long id, String title, String imageUrl, String linkUrl) {
        return BannerSlide.reconstitute(
                BannerSlideId.of(id),
                title,
                imageUrl,
                linkUrl,
                1,
                DisplayPeriod.of(
                        Instant.parse("2000-01-01T00:00:00Z"),
                        Instant.parse("2099-12-31T23:59:59Z")),
                true,
                DeletionStatus.active(),
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-01T00:00:00Z"));
    }

    public static List<BannerSlide> bannerSlideList() {
        return List.of(
                bannerSlide(1L),
                bannerSlide(2L, "여름 세일", "https://cdn.example.com/banner/2.jpg", "/sale/summer"));
    }

    // ===== BannerSlideV1ApiResponse Fixtures =====

    public static BannerSlideV1ApiResponse bannerSlideResponse(long id) {
        return new BannerSlideV1ApiResponse(
                id, "신규 회원 이벤트", "https://cdn.example.com/banner/1.jpg", "/event/new-member");
    }

    public static BannerSlideV1ApiResponse bannerSlideResponse(
            long id, String title, String imageUrl, String linkUrl) {
        return new BannerSlideV1ApiResponse(id, title, imageUrl, linkUrl);
    }

    public static List<BannerSlideV1ApiResponse> bannerSlideResponseList() {
        return List.of(
                bannerSlideResponse(1L),
                bannerSlideResponse(
                        2L, "여름 세일", "https://cdn.example.com/banner/2.jpg", "/sale/summer"));
    }
}
