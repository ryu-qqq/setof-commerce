package com.ryuqq.setof.domain.seller.query;

import com.ryuqq.setof.domain.common.vo.SearchField;

/** 셀러 주소 검색 필드. */
public enum SellerAddressSearchField implements SearchField {
    ADDRESS_NAME("addressName"),

    ADDRESS("address");

    private final String fieldName;

    SellerAddressSearchField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static SellerAddressSearchField fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (SellerAddressSearchField field : values()) {
            if (field.fieldName.equalsIgnoreCase(value) || field.name().equalsIgnoreCase(value)) {
                return field;
            }
        }
        return null;
    }
}
