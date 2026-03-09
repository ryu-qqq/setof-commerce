package com.ryuqq.setof.adapter.in.rest.v1.banner;

/**
 * BannerV1Endpoints - 배너 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>레거시 ContentController 경로 호환: /api/v1/content/banner
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BannerV1Endpoints {

    private BannerV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 배너 조회 경로 (GET /api/v1/content/banner?bannerType=...) - 레거시 호환 */
    public static final String BANNERS = BASE_V1 + "/content/banner";
}
