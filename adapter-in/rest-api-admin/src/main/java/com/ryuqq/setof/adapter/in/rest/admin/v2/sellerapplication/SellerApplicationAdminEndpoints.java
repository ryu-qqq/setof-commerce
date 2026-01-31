package com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication;

/**
 * SellerApplicationAdminEndpoints - 셀러 입점 신청 Admin API 엔드포인트 상수.
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
public final class SellerApplicationAdminEndpoints {

    private SellerApplicationAdminEndpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** 셀러 입점 신청 기본 경로 */
    public static final String SELLER_APPLICATIONS = "/api/v2/admin/seller-applications";

    /** ID Path Variable */
    public static final String ID = "/{applicationId}";

    /** ID Path Variable 이름 */
    public static final String PATH_APPLICATION_ID = "applicationId";

    /** 승인 경로 */
    public static final String APPROVE = "/{applicationId}/approve";

    /** 거절 경로 */
    public static final String REJECT = "/{applicationId}/reject";
}
