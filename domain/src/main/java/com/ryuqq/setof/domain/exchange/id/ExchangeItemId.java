package com.ryuqq.setof.domain.exchange.id;

/** 교환 아이템 ID Value Object. */
public record ExchangeItemId(Long value) {

    public static ExchangeItemId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ExchangeItemId 값은 null일 수 없습니다");
        }
        return new ExchangeItemId(value);
    }

    public static ExchangeItemId forNew() {
        return new ExchangeItemId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
