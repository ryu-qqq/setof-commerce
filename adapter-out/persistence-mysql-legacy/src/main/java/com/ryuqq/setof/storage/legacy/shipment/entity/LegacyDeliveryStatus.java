package com.ryuqq.setof.storage.legacy.shipment.entity;

/**
 * LegacyDeliveryStatus - 레거시 배송 상태 Enum.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum LegacyDeliveryStatus {
    PENDING("배송 준비중"),
    PROCESSING("배송중"),
    COMPLETED("배송 완료"),
    CANCELLED("배송 취소");

    private final String displayName;

    LegacyDeliveryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
