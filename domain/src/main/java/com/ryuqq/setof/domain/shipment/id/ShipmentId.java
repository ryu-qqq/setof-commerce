package com.ryuqq.setof.domain.shipment.id;

/** 배송 ID Value Object. */
public record ShipmentId(Long value) {

    public static ShipmentId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ShipmentId 값은 null일 수 없습니다");
        }
        return new ShipmentId(value);
    }

    public static ShipmentId forNew() {
        return new ShipmentId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
