package com.ryuqq.setof.domain.settlement.vo;

import java.util.Map;
import java.util.Set;

/** 정산 상태. */
public enum SettlementStatus {
    PENDING("대기"),
    PROCESSING("처리중"),
    COMPLETED("완료");

    private static final Map<SettlementStatus, Set<SettlementStatus>> VALID_TRANSITIONS =
            Map.of(
                    PENDING, Set.of(PROCESSING),
                    PROCESSING, Set.of(COMPLETED));

    private final String description;

    SettlementStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 현재 상태에서 대상 상태로 전이 가능한지 확인합니다.
     *
     * @param target 전이 대상 상태
     * @return 전이 가능 여부
     */
    public boolean canTransitionTo(SettlementStatus target) {
        Set<SettlementStatus> allowed = VALID_TRANSITIONS.get(this);
        return allowed != null && allowed.contains(target);
    }

    /**
     * 정산이 완료된 상태인지 확인합니다.
     *
     * @return 완료 여부
     */
    public boolean isCompleted() {
        return this == COMPLETED;
    }
}
