package com.ryuqq.setof.domain.checkout.vo;

/**
 * CheckoutStatus - 결제 세션 상태
 *
 * <p>결제 세션의 생명주기를 나타내는 상태값입니다.
 *
 * <p>상태 흐름:
 *
 * <pre>
 * PENDING → PROCESSING → COMPLETED
 *    │
 *    └→ EXPIRED
 * </pre>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java enum 사용
 *   <li>상태 전이 검증 메서드 제공
 * </ul>
 */
public enum CheckoutStatus {

    /** 결제 대기 중 (장바구니 → 결제 페이지 진입) */
    PENDING("결제 대기"),

    /** 결제 처리 중 (PG 결제 요청 진행 중) */
    PROCESSING("결제 처리 중"),

    /** 결제 완료 (결제 성공 → 주문 생성됨) */
    COMPLETED("결제 완료"),

    /** 세션 만료 (유효 시간 초과) */
    EXPIRED("세션 만료");

    private final String description;

    CheckoutStatus(String description) {
        this.description = description;
    }

    /**
     * 상태 설명 반환
     *
     * @return 상태 설명
     */
    public String description() {
        return description;
    }

    /**
     * 기본 상태 반환
     *
     * @return PENDING 상태
     */
    public static CheckoutStatus defaultStatus() {
        return PENDING;
    }

    /**
     * 결제 처리 시작 가능 여부
     *
     * @return PENDING 상태이면 true
     */
    public boolean canStartProcessing() {
        return this == PENDING;
    }

    /**
     * 결제 완료 가능 여부
     *
     * @return PROCESSING 상태이면 true
     */
    public boolean canComplete() {
        return this == PROCESSING;
    }

    /**
     * 만료 가능 여부
     *
     * @return PENDING 또는 PROCESSING 상태이면 true
     */
    public boolean canExpire() {
        return this == PENDING || this == PROCESSING;
    }

    /**
     * 최종 상태 여부 (더 이상 상태 변경 불가)
     *
     * @return COMPLETED 또는 EXPIRED이면 true
     */
    public boolean isFinal() {
        return this == COMPLETED || this == EXPIRED;
    }
}
