package com.ryuqq.setof.domain.cancel.vo;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * 취소 상태.
 *
 * <p>취소의 생명주기를 나타내며, 상태 전이 규칙을 정의합니다.
 *
 * <p><strong>상태 전이 규칙:</strong>
 *
 * <ul>
 *   <li>REQUESTED -> APPROVED, REJECTED, CANCELLED
 *   <li>APPROVED -> COMPLETED
 *   <li>REJECTED, COMPLETED, CANCELLED -> (종료 상태)
 * </ul>
 */
public enum CancelStatus {
    REQUESTED("요청됨"),
    APPROVED("승인됨"),
    REJECTED("거부됨"),
    COMPLETED("완료됨"),
    CANCELLED("철회됨");

    private static final Map<CancelStatus, Set<CancelStatus>> VALID_TRANSITIONS;

    static {
        VALID_TRANSITIONS = new EnumMap<>(CancelStatus.class);
        VALID_TRANSITIONS.put(REQUESTED, Set.of(APPROVED, REJECTED, CANCELLED));
        VALID_TRANSITIONS.put(APPROVED, Set.of(COMPLETED));
    }

    private final String description;

    CancelStatus(String description) {
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
    public boolean canTransitionTo(CancelStatus target) {
        Set<CancelStatus> allowed = VALID_TRANSITIONS.get(this);
        return allowed != null && allowed.contains(target);
    }

    /**
     * 종료 상태인지 확인합니다.
     *
     * <p>REJECTED, COMPLETED, CANCELLED는 종료 상태입니다.
     *
     * @return 종료 상태 여부
     */
    public boolean isFinal() {
        return this == REJECTED || this == COMPLETED || this == CANCELLED;
    }

    /**
     * 활성 상태인지 확인합니다.
     *
     * <p>종료 상태가 아닌 REQUESTED, APPROVED는 활성 상태입니다.
     *
     * @return 활성 상태 여부
     */
    public boolean isActive() {
        return !isFinal();
    }
}
