package com.ryuqq.setof.adapter.in.rest.admin.v1.seller;

/**
 * SellerAdminV1Endpoints - 셀러 Admin V1 API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>API-END-003: Path Variable 상수.
 *
 * <p>레거시 SellerController 경로 호환: /api/v1/sellers
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class SellerAdminV1Endpoints {

    private SellerAdminV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    // ========================================================================
    // 셀러 경로
    // ========================================================================

    /** 셀러 목록/검색 조회 경로 (GET /api/v1/sellers) */
    public static final String SELLERS = BASE_V1 + "/sellers";

    /** 셀러 단건 조회 경로 (GET /api/v1/sellers/{sellerId}) */
    public static final String SELLER_ID = "/{sellerId}";

    /** 셀러 단건 전체 경로 */
    public static final String SELLER_BY_ID = SELLERS + SELLER_ID;

    /** 사업자등록번호 유효성 검증 경로 (GET /api/v1/sellers/business-validation) */
    public static final String BUSINESS_VALIDATION = "/business-validation";

    // ========================================================================
    // Path Variable 상수
    // ========================================================================

    /** Seller ID Path Variable 이름 */
    public static final String PATH_SELLER_ID = "sellerId";
}
