package com.ryuqq.setof.domain.payment.vo;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * 결제 상태 열거형.
 *
 * <p>결제의 생명주기 상태를 나타내며, 허용된 상태 전이를 관리합니다.
 */
public enum PaymentStatus {
    PROCESSING("처리중"),
    COMPLETED("완료"),
    PARTIALLY_REFUNDED("부분환불"),
    REFUNDED("전액환불"),
    FAILED("실패"),
    CANCELLED("취소");

    private final String description;

    private static final Map<PaymentStatus, Set<PaymentStatus>> VALID_TRANSITIONS;

    static {
        VALID_TRANSITIONS = new EnumMap<>(PaymentStatus.class);
        VALID_TRANSITIONS.put(PROCESSING, Set.of(COMPLETED, FAILED, CANCELLED));
        VALID_TRANSITIONS.put(COMPLETED, Set.of(PARTIALLY_REFUNDED, REFUNDED));
        VALID_TRANSITIONS.put(PARTIALLY_REFUNDED, Set.of(REFUNDED));
        VALID_TRANSITIONS.put(REFUNDED, Set.of());
        VALID_TRANSITIONS.put(FAILED, Set.of());
        VALID_TRANSITIONS.put(CANCELLED, Set.of());
    }

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 현재 상태에서 대상 상태로 전이 가능한지 확인합니다.
     *
     * @param target 대상 상태
     * @return 전이 가능 여부
     */
    public boolean canTransitionTo(PaymentStatus target) {
        return VALID_TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }

    /**
     * 환불 가능한 상태인지 확인합니다.
     *
     * @return COMPLETED 또는 PARTIALLY_REFUNDED인 경우 true
     */
    public boolean isRefundable() {
        return this == COMPLETED || this == PARTIALLY_REFUNDED;
    }

    /**
     * 결제 완료 상태인지 확인합니다.
     *
     * @return COMPLETED인 경우 true
     */
    public boolean isCompleted() {
        return this == COMPLETED;
    }

    /**
     * 최종 상태인지 확인합니다. 최종 상태에서는 더 이상 전이가 불가합니다.
     *
     * @return REFUNDED, FAILED, CANCELLED인 경우 true
     */
    public boolean isFinal() {
        return this == REFUNDED || this == FAILED || this == CANCELLED;
    }
}
