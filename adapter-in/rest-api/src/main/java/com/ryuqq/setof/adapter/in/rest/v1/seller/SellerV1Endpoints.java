package com.ryuqq.setof.adapter.in.rest.v1.seller;

/**
 * SellerV1Endpoints - 셀러 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>API-END-003: Path Variable 상수.
 *
 * <p>레거시 SellerController 경로 호환: /api/v1/seller
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
    // 셀러 경로
    // ========================================================================

    /** 셀러 기본 경로 (GET /api/v1/seller) */
    public static final String SELLERS = BASE_V1 + "/seller";

    /** 셀러 단건 조회 경로 (GET /api/v1/seller/{sellerId}) */
    public static final String SELLER_ID = "/{sellerId}";

    /** 셀러 단건 전체 경로 */
    public static final String SELLER_BY_ID = SELLERS + SELLER_ID;

    // ========================================================================
    // Path Variable 상수
    // ========================================================================

    /** Seller ID Path Variable 이름 */
    public static final String PATH_SELLER_ID = "sellerId";
}
