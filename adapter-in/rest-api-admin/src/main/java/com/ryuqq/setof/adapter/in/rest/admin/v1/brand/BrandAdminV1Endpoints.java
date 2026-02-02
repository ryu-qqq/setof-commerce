package com.ryuqq.setof.adapter.in.rest.admin.v1.brand;

/**
 * BrandAdminV1Endpoints - 브랜드 Admin V1 API 엔드포인트 상수.
 *
 * <p>레거시 호환을 위한 V1 엔드포인트 정의.
 *
 * <p>API-END-001: Endpoints final class
 *
 * <p>API-END-002: static final 상수
 *
 * <p>API-END-003: Path Variable 상수
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
    // 브랜드 조회 경로
    // ========================================================================

    /** 브랜드 목록 조회 경로 */
    public static final String BRANDS = BASE_V1 + "/brands";

    // ========================================================================
    // 브랜드 매핑 경로
    // ========================================================================

    /** 외부 브랜드 매핑 경로 */
    public static final String BRAND_EXTERNAL_MAPPING =
            BASE_V1 + "/brand/external/{siteId}/mapping";

    // ========================================================================
    // Path Variable 상수
    // ========================================================================

    /** Site ID Path Variable 이름 */
    public static final String PATH_SITE_ID = "siteId";
}
