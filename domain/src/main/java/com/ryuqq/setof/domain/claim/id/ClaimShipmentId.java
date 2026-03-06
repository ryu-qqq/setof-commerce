package com.ryuqq.setof.domain.claim.id;

/** 클레임 배송 ID Value Object. */
public record ClaimShipmentId(Long value) {

    public static ClaimShipmentId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ClaimShipmentId 값은 null일 수 없습니다");
        }
        return new ClaimShipmentId(value);
    }

    public static ClaimShipmentId forNew() {
        return new ClaimShipmentId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
