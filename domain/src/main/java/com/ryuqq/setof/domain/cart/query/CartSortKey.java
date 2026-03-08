package com.ryuqq.setof.domain.cart.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * 장바구니 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum CartSortKey implements SortKey {
    CREATED_AT("createdAt");

    private final String fieldName;

    CartSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static CartSortKey defaultKey() {
        return CREATED_AT;
    }
}
