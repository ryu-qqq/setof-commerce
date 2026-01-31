package com.ryuqq.setof.adapter.in.rest.admin.v2.seller;

/**
 * SellerAdminV2Endpoints - 셀러 Admin V2 API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>API-END-003: Path Variable 상수.
 *
 * <p>API-CTR-012: URL 경로 소문자 + 복수형.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class SellerAdminV2Endpoints {

    private SellerAdminV2Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V2 기본 경로 */
    public static final String BASE_V2 = "/api/v2/admin";

    // ========================================================================
    // 셀러 경로
    // ========================================================================

    /** 셀러 목록 조회 경로 */
    public static final String SELLERS = BASE_V2 + "/sellers";

    /** 셀러 단건 조회 경로 */
    public static final String SELLER_ID = "/{sellerId}";

    /** 셀러 단건 전체 경로 */
    public static final String SELLER_BY_ID = SELLERS + SELLER_ID;

    /** 셀러 삭제 경로 */
    public static final String SELLER_DELETE = SELLER_ID + "/delete";

    // ========================================================================
    // Path Variable 상수
    // ========================================================================

    /** Seller ID Path Variable 이름 */
    public static final String PATH_SELLER_ID = "sellerId";
}
