package com.ryuqq.setof.adapter.in.rest.admin.v2.seller;

/**
 * SellerAdminEndpoints - 셀러 Admin API 엔드포인트 상수.
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
public final class SellerAdminEndpoints {

    private SellerAdminEndpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** 셀러 기본 경로 */
    public static final String SELLERS = "/api/v2/sellers";

    /** Seller ID Path Variable */
    public static final String ID = "/{sellerId}";

    /** Seller ID Path Variable 이름 */
    public static final String PATH_SELLER_ID = "sellerId";
}
