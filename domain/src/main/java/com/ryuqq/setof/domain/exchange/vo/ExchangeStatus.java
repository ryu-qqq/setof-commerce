package com.ryuqq.setof.domain.exchange.vo;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * 교환 상태.
 *
 * <p>교환의 생명주기를 나타내며, 상태 전이 규칙을 정의합니다.
 *
 * <p><strong>상태 전이 규칙:</strong>
 *
 * <ul>
 *   <li>REQUESTED -> COLLECTING, REJECTED, CANCELLED
 *   <li>COLLECTING -> COLLECTED, REJECTED
 *   <li>COLLECTED -> PREPARING, REJECTED
 *   <li>PREPARING -> SHIPPING
 *   <li>SHIPPING -> COMPLETED
 *   <li>COMPLETED, REJECTED, CANCELLED -> (종료 상태)
 * </ul>
 */
public enum ExchangeStatus {
    REQUESTED("요청됨"),
    COLLECTING("수거중"),
    COLLECTED("수거완료"),
    PREPARING("준비중"),
    SHIPPING("배송중"),
    COMPLETED("완료됨"),
    REJECTED("거부됨"),
    CANCELLED("철회됨");

    private static final Map<ExchangeStatus, Set<ExchangeStatus>> VALID_TRANSITIONS;

    static {
        VALID_TRANSITIONS = new EnumMap<>(ExchangeStatus.class);
        VALID_TRANSITIONS.put(REQUESTED, Set.of(COLLECTING, REJECTED, CANCELLED));
        VALID_TRANSITIONS.put(COLLECTING, Set.of(COLLECTED, REJECTED));
        VALID_TRANSITIONS.put(COLLECTED, Set.of(PREPARING, REJECTED));
        VALID_TRANSITIONS.put(PREPARING, Set.of(SHIPPING));
        VALID_TRANSITIONS.put(SHIPPING, Set.of(COMPLETED));
    }

    private final String description;

    ExchangeStatus(String description) {
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
    public boolean canTransitionTo(ExchangeStatus target) {
        Set<ExchangeStatus> allowed = VALID_TRANSITIONS.get(this);
        return allowed != null && allowed.contains(target);
    }

    /**
     * 종료 상태인지 확인합니다.
     *
     * <p>COMPLETED, REJECTED, CANCELLED는 종료 상태입니다.
     *
     * @return 종료 상태 여부
     */
    public boolean isFinal() {
        return this == COMPLETED || this == REJECTED || this == CANCELLED;
    }

    /**
     * 활성 상태인지 확인합니다.
     *
     * <p>종료 상태가 아닌 REQUESTED, COLLECTING, COLLECTED, PREPARING, SHIPPING은 활성 상태입니다.
     *
     * @return 활성 상태 여부
     */
    public boolean isActive() {
        return !isFinal();
    }

    /**
     * 수거 단계인지 확인합니다.
     *
     * <p>COLLECTING, COLLECTED는 수거 단계입니다.
     *
     * @return 수거 단계 여부
     */
    public boolean isCollectionStep() {
        return this == COLLECTING || this == COLLECTED;
    }

    /**
     * 배송 단계인지 확인합니다.
     *
     * <p>SHIPPING, COMPLETED는 배송 단계입니다.
     *
     * @return 배송 단계 여부
     */
    public boolean isShippingStep() {
        return this == SHIPPING || this == COMPLETED;
    }
}
