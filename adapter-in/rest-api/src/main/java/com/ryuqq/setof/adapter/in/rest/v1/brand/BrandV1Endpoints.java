package com.ryuqq.setof.adapter.in.rest.v1.brand;

/**
 * BrandV1Endpoints - 브랜드 V1 API 엔드포인트 상수.
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
public final class BrandV1Endpoints {

    private BrandV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    // ========================================================================
    // 브랜드 조회 경로
    // ========================================================================

    /** 브랜드 목록 조회 경로 */
    public static final String BRAND = BASE_V1 + "/brand";

    /** 브랜드 단건 조회 경로 */
    public static final String BRAND_BY_ID = BASE_V1 + "/brand/{brandId}";

    // ========================================================================
    // Path Variable 상수
    // ========================================================================

    /** Brand ID Path Variable 이름 */
    public static final String PATH_BRAND_ID = "brandId";
}
