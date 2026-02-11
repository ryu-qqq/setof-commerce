package com.ryuqq.setof.adapter.in.rest.admin.v1.brand;

/**
 * BrandAdminV1Endpoints - 브랜드 Admin V1 API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>레거시 BrandController 경로 호환: /api/v1/brands
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class BrandAdminV1Endpoints {

    private BrandAdminV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    // ========================================================================
    // 브랜드 경로
    // ========================================================================

    /** 브랜드 목록/검색 조회 경로 (GET /api/v1/brands) */
    public static final String BRANDS = BASE_V1 + "/brands";
}
