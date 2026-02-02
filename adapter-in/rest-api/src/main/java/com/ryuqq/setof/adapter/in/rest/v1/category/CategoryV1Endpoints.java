package com.ryuqq.setof.adapter.in.rest.v1.category;

/**
 * CategoryV1Endpoints - 카테고리 V1 API 엔드포인트 상수.
 *
 * <p>레거시 호환을 위한 V1 엔드포인트 정의.
 *
 * <p>API-END-001: Endpoints final class
 *
 * <p>API-END-002: static final 상수
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class CategoryV1Endpoints {

    private CategoryV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    // ========================================================================
    // 카테고리 조회 경로
    // ========================================================================

    /** 전체 카테고리 트리 조회 경로 */
    public static final String CATEGORY = BASE_V1 + "/category";
}
