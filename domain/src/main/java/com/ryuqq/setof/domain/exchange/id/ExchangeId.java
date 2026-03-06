package com.ryuqq.setof.domain.exchange.id;

/** 교환 ID Value Object. */
public record ExchangeId(Long value) {

    public static ExchangeId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ExchangeId 값은 null일 수 없습니다");
        }
        return new ExchangeId(value);
    }

    public static ExchangeId forNew() {
        return new ExchangeId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
