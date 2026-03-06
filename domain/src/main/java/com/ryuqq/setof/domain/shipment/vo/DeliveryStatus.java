package com.ryuqq.setof.domain.shipment.vo;

import java.util.Map;
import java.util.Set;

/** 배송 상태. */
public enum DeliveryStatus {
    PENDING("대기"),
    PROCESSING("처리중"),
    COMPLETED("배송완료"),
    RETURN_REQUESTED("반송요청"),
    RETURN_PROCESSING("반송처리중"),
    RETURN_COMPLETED("반송완료");

    private static final Map<DeliveryStatus, Set<DeliveryStatus>> VALID_TRANSITIONS =
            Map.of(
                    PENDING, Set.of(PROCESSING),
                    PROCESSING, Set.of(COMPLETED),
                    COMPLETED, Set.of(RETURN_REQUESTED),
                    RETURN_REQUESTED, Set.of(RETURN_PROCESSING),
                    RETURN_PROCESSING, Set.of(RETURN_COMPLETED));

    private final String description;

    DeliveryStatus(String description) {
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
    public boolean canTransitionTo(DeliveryStatus target) {
        Set<DeliveryStatus> allowed = VALID_TRANSITIONS.get(this);
        return allowed != null && allowed.contains(target);
    }

    /**
     * 배송 완료 상태인지 확인합니다.
     *
     * @return 배송 완료 여부
     */
    public boolean isCompleted() {
        return this == COMPLETED;
    }

    /**
     * 반송 단계 상태인지 확인합니다.
     *
     * @return 반송 단계 여부
     */
    public boolean isReturnPhase() {
        return this == RETURN_REQUESTED || this == RETURN_PROCESSING || this == RETURN_COMPLETED;
    }
}
