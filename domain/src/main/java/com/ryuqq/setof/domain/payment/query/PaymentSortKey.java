package com.ryuqq.setof.domain.payment.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * 결제 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum PaymentSortKey implements SortKey {
    PAYMENT_DATE("insertDate");

    private final String fieldName;

    PaymentSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static PaymentSortKey defaultKey() {
        return PAYMENT_DATE;
    }
}
