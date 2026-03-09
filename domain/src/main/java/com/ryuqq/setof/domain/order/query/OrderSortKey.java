package com.ryuqq.setof.domain.order.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * 주문 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum OrderSortKey implements SortKey {
    ORDER_DATE("insertDate");

    private final String fieldName;

    OrderSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static OrderSortKey defaultKey() {
        return ORDER_DATE;
    }
}
