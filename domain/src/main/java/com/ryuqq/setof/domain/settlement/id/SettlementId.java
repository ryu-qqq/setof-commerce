package com.ryuqq.setof.domain.settlement.id;

/** 정산 ID Value Object. */
public record SettlementId(Long value) {

    public static SettlementId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("SettlementId 값은 null일 수 없습니다");
        }
        return new SettlementId(value);
    }

    public static SettlementId forNew() {
        return new SettlementId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
