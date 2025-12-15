package com.setof.connectly.module.discount.enums;

import com.setof.connectly.module.common.enums.EnumType;

public enum IssueType implements EnumType {
    PRODUCT,
    SELLER,
    BRAND;

    public boolean isProductIssue() {
        return this.equals(PRODUCT);
    }

    public boolean isSellerIssue() {
        return this.equals(SELLER);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}
