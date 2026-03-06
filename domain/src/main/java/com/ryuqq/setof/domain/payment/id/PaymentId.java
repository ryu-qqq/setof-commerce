package com.ryuqq.setof.domain.payment.id;

/** 결제 ID Value Object. */
public record PaymentId(Long value) {

    public static PaymentId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("PaymentId 값은 null일 수 없습니다");
        }
        return new PaymentId(value);
    }

    public static PaymentId forNew() {
        return new PaymentId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
