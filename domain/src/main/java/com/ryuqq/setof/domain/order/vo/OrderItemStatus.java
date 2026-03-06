package com.ryuqq.setof.domain.order.vo;

import java.util.Map;
import java.util.Set;

/** 주문 아이템 상태. */
public enum OrderItemStatus {
    PENDING("대기"),
    CONFIRMED("확인"),
    SHIPPING_READY("배송준비"),
    SHIPPED("배송중"),
    DELIVERED("배송완료"),
    PURCHASE_CONFIRMED("구매확정"),
    SETTLEMENT_PENDING("정산대기"),
    SETTLED("정산완료");

    private static final Map<OrderItemStatus, Set<OrderItemStatus>> VALID_TRANSITIONS =
            Map.of(
                    PENDING, Set.of(CONFIRMED),
                    CONFIRMED, Set.of(SHIPPING_READY),
                    SHIPPING_READY, Set.of(SHIPPED),
                    SHIPPED, Set.of(DELIVERED),
                    DELIVERED, Set.of(PURCHASE_CONFIRMED),
                    PURCHASE_CONFIRMED, Set.of(SETTLEMENT_PENDING),
                    SETTLEMENT_PENDING, Set.of(SETTLED));

    private final String description;

    OrderItemStatus(String description) {
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
    public boolean canTransitionTo(OrderItemStatus target) {
        Set<OrderItemStatus> allowed = VALID_TRANSITIONS.get(this);
        return allowed != null && allowed.contains(target);
    }

    /** 환불 가능 여부. PENDING ~ DELIVERED 단계에서 환불 가능합니다. */
    public boolean isRefundable() {
        return this == PENDING
                || this == CONFIRMED
                || this == SHIPPING_READY
                || this == SHIPPED
                || this == DELIVERED;
    }

    /** 배송 관련 단계 여부. */
    public boolean isDeliveryStep() {
        return this == SHIPPING_READY || this == SHIPPED || this == DELIVERED;
    }

    /** 정산 완료 여부. */
    public boolean isSettled() {
        return this == SETTLED;
    }
}
