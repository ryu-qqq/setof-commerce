package com.ryuqq.setof.adapter.in.rest.v1.brand;

/**
 * BrandV1Endpoints - 브랜드 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>API-END-003: Path Variable 상수.
 *
 * <p>레거시 BrandController 경로 호환: /api/v1/brand
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
    // 브랜드 경로
    // ========================================================================

    /** 브랜드 목록/검색 조회 경로 (GET /api/v1/brand) */
    public static final String BRANDS = BASE_V1 + "/brand";

    /** 브랜드 단건 조회 경로 (GET /api/v1/brand/{brandId}) */
    public static final String BRAND_ID = "/{brandId}";

    /** 브랜드 단건 전체 경로 */
    public static final String BRAND_BY_ID = BRANDS + BRAND_ID;

    // ========================================================================
    // Path Variable 상수
    // ========================================================================

    /** Brand ID Path Variable 이름 */
    public static final String PATH_BRAND_ID = "brandId";
}
