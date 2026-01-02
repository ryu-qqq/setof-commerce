package com.connectly.partnerAdmin.module.discount.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum IssueType implements EnumType {

    PRODUCT,
    SELLER,
    BRAND;

    public boolean isProductIssue(){
        return this.equals(PRODUCT);
    }

    public boolean isSellerIssue(){
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
