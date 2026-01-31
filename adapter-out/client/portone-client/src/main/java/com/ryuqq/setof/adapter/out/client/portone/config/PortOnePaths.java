package com.ryuqq.setof.adapter.out.client.portone.config;

/**
 * PortOne V2 API 경로 관리
 *
 * <p>모든 PortOne API 경로를 중앙 집중 관리합니다.
 *
 * <p><strong>경로 카테고리:</strong>
 *
 * <ul>
 *   <li>인증: 토큰 발급/갱신 관련 경로
 *   <li>은행: 가상계좌, 계좌 검증 관련 경로
 *   <li>결제: 결제 처리 관련 경로 (향후 추가)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public final class PortOnePaths {

    private PortOnePaths() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ========================================
    // 인증 관련 경로
    // ========================================

    /** API Secret 로그인 경로 */
    public static final String AUTH_LOGIN = "/login/api-secret";

    /** 토큰 갱신 경로 */
    public static final String AUTH_REFRESH = "/token/refresh";

    // ========================================
    // 은행/계좌 관련 경로
    // ========================================

    /** 가상계좌 예금주 조회 경로 */
    public static final String VBANK_HOLDER = "/vbanks/holder";

    // ========================================
    // 결제 관련 경로 (향후 확장)
    // ========================================

    // 결제 조회: /payments/{payment_id}
    // 결제 취소: /payments/{payment_id}/cancel
    // 빌링키 발급: /billing-keys
}
