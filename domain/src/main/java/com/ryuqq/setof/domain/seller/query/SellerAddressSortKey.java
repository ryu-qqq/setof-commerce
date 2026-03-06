package com.ryuqq.setof.domain.seller.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/** 셀러 주소 정렬 키. */
public enum SellerAddressSortKey implements SortKey {
    CREATED_AT("createdAt");

    private final String fieldName;

    SellerAddressSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static SellerAddressSortKey defaultKey() {
        return CREATED_AT;
    }
}
