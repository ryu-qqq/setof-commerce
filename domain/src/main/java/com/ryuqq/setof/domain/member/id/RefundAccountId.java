package com.ryuqq.setof.domain.member.id;

/**
 * 환불 계좌 ID Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RefundAccountId(Long value) {

    public static RefundAccountId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("RefundAccountId 값은 null일 수 없습니다");
        }
        return new RefundAccountId(value);
    }

    public static RefundAccountId forNew() {
        return new RefundAccountId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
