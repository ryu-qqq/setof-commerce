package com.ryuqq.setof.adapter.in.rest.v1.seller;

/**
 * SellerV1Endpoints - 셀러 V1 API 엔드포인트 상수.
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
public final class SellerV1Endpoints {

    private SellerV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    // ========================================================================
    // 셀러 조회 경로
    // ========================================================================

    /** 셀러 기본 경로 */
    public static final String SELLER = BASE_V1 + "/seller";

    /** 특정 셀러 조회 경로 */
    public static final String SELLER_BY_ID = SELLER + "/{sellerId}";

    // ========================================================================
    // Path Variable 상수
    // ========================================================================

    /** Seller ID Path Variable 이름 */
    public static final String PATH_SELLER_ID = "sellerId";
}
