package com.ryuqq.setof.adapter.in.rest.admin.v1.seller;

/**
 * SellerAdminV1Endpoints - 셀러 Admin V1 API 엔드포인트 상수.
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
public final class SellerAdminV1Endpoints {

    private SellerAdminV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    // ========================================================================
    // 셀러 단건 조회/수정 경로
    // ========================================================================

    /** 인증된 셀러 본인 조회 경로 */
    public static final String SELLER = BASE_V1 + "/seller";

    /** 특정 셀러 조회/수정 경로 */
    public static final String SELLER_BY_ID = BASE_V1 + "/seller/{sellerId}";

    // ========================================================================
    // 셀러 목록/일괄 처리 경로
    // ========================================================================

    /** 셀러 목록 조회 경로 */
    public static final String SELLERS = BASE_V1 + "/sellers";

    /** 사업자등록번호 검증 경로 */
    public static final String SELLERS_BUSINESS_VALIDATION = SELLERS + "/business-validation";

    // ========================================================================
    // 셀러 부가 기능 경로
    // ========================================================================

    /** 셀러 정산 계좌 확인 경로 */
    public static final String SELLER_ACCOUNT = BASE_V1 + "/seller-account";

    /** 셀러 승인 상태 변경 경로 */
    public static final String SELLER_APPROVAL_STATUS = SELLER + "/approval-status";

    // ========================================================================
    // Path Variable 상수
    // ========================================================================

    /** Seller ID Path Variable 이름 */
    public static final String PATH_SELLER_ID = "sellerId";
}
