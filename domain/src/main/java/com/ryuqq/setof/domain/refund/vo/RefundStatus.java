package com.ryuqq.setof.domain.refund.vo;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * 반품 상태.
 *
 * <p>반품 프로세스의 상태를 나타내며, 상태 전이 규칙을 캡슐화합니다.
 *
 * <ul>
 *   <li>REQUESTED → COLLECTING, REJECTED, CANCELLED
 *   <li>COLLECTING → COLLECTED, REJECTED
 *   <li>COLLECTED → COMPLETED, REJECTED
 *   <li>COMPLETED, REJECTED, CANCELLED → (종료 상태)
 * </ul>
 */
public enum RefundStatus {
    REQUESTED("요청됨"),
    COLLECTING("수거중"),
    COLLECTED("수거완료"),
    COMPLETED("완료됨"),
    REJECTED("거부됨"),
    CANCELLED("철회됨");

    private static final Map<RefundStatus, Set<RefundStatus>> VALID_TRANSITIONS;

    static {
        VALID_TRANSITIONS = new EnumMap<>(RefundStatus.class);
        VALID_TRANSITIONS.put(REQUESTED, Set.of(COLLECTING, REJECTED, CANCELLED));
        VALID_TRANSITIONS.put(COLLECTING, Set.of(COLLECTED, REJECTED));
        VALID_TRANSITIONS.put(COLLECTED, Set.of(COMPLETED, REJECTED));
    }

    private final String description;

    RefundStatus(String description) {
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
    public boolean canTransitionTo(RefundStatus target) {
        return VALID_TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }

    /**
     * 종료 상태인지 확인합니다.
     *
     * @return 종료 상태 여부 (COMPLETED, REJECTED, CANCELLED)
     */
    public boolean isFinal() {
        return this == COMPLETED || this == REJECTED || this == CANCELLED;
    }

    /**
     * 활성 상태인지 확인합니다.
     *
     * @return 활성 상태 여부 (종료 상태가 아닌 경우)
     */
    public boolean isActive() {
        return !isFinal();
    }

    /**
     * 수거 단계인지 확인합니다.
     *
     * @return 수거 단계 여부 (COLLECTING, COLLECTED)
     */
    public boolean isCollectionStep() {
        return this == COLLECTING || this == COLLECTED;
    }
}
