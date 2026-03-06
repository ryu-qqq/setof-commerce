package com.ryuqq.setof.domain.order.vo;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * 주문 상태.
 *
 * <p>2단계 주문 패턴을 따릅니다:
 *
 * <ul>
 *   <li>PENDING: 주문 생성, 결제 대기 (재고 선점 상태)
 *   <li>PLACED: 결제 완료, 주문 확정
 *   <li>CONFIRMED: 판매자 확인
 *   <li>COMPLETED: 전체 처리 완료
 *   <li>FAILED: 결제 실패
 *   <li>EXPIRED: 결제 타임아웃 (TTL 만료)
 *   <li>CANCELLED: 주문 취소
 * </ul>
 *
 * <p>상태 전이 규칙:
 *
 * <ul>
 *   <li>PENDING → PLACED, FAILED, EXPIRED
 *   <li>PLACED → CONFIRMED, CANCELLED
 *   <li>CONFIRMED → COMPLETED
 *   <li>FAILED, EXPIRED, COMPLETED, CANCELLED → (종료 상태)
 * </ul>
 */
public enum OrderStatus {
    PENDING("결제대기"),
    PLACED("주문확정"),
    CONFIRMED("확인됨"),
    COMPLETED("완료"),
    FAILED("결제실패"),
    EXPIRED("만료됨"),
    CANCELLED("취소됨");

    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS;

    static {
        VALID_TRANSITIONS = new EnumMap<>(OrderStatus.class);
        VALID_TRANSITIONS.put(PENDING, Set.of(PLACED, FAILED, EXPIRED));
        VALID_TRANSITIONS.put(PLACED, Set.of(CONFIRMED, CANCELLED));
        VALID_TRANSITIONS.put(CONFIRMED, Set.of(COMPLETED));
    }

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    /**
     * 현재 상태에서 대상 상태로 전이 가능한지 확인합니다.
     *
     * @param target 전이 대상 상태
     * @return 전이 가능 여부
     */
    public boolean canTransitionTo(OrderStatus target) {
        return VALID_TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }

    /**
     * 종료 상태인지 확인합니다.
     *
     * @return FAILED, EXPIRED, COMPLETED, CANCELLED이면 true
     */
    public boolean isFinal() {
        return this == FAILED || this == EXPIRED || this == COMPLETED || this == CANCELLED;
    }

    /**
     * 결제 대기 상태인지 확인합니다.
     *
     * @return PENDING이면 true
     */
    public boolean isPending() {
        return this == PENDING;
    }

    /**
     * 주문이 확정된 상태인지 확인합니다.
     *
     * @return PLACED, CONFIRMED, COMPLETED이면 true
     */
    public boolean isConfirmed() {
        return this == PLACED || this == CONFIRMED || this == COMPLETED;
    }
}
