package com.ryuqq.setof.adapter.in.rest.v1.refundaccount;

/**
 * RefundAccountV1Endpoints - 환불 계좌 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>API-END-003: Path Variable 상수.
 *
 * <p>레거시 UserController 경로 호환: /api/v1/user/refund-account
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class RefundAccountV1Endpoints {

    private RefundAccountV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 환불 계좌 경로 (GET /api/v1/user/refund-account) */
    public static final String REFUND_ACCOUNT = BASE_V1 + "/user/refund-account";

    /** 환불 계좌 단건 경로 변수 */
    public static final String REFUND_ACCOUNT_ID = "/{refundAccountId}";

    /** 환불 계좌 단건 전체 경로 */
    public static final String REFUND_ACCOUNT_BY_ID = REFUND_ACCOUNT + REFUND_ACCOUNT_ID;

    /** RefundAccount ID Path Variable 이름 */
    public static final String PATH_REFUND_ACCOUNT_ID = "refundAccountId";
}
