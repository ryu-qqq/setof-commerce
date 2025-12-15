package com.setof.connectly.module.search.enums;

import com.setof.connectly.module.common.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchKeyword implements EnumType {
    PRODUCT_GROUP_NAME("productGroupName"),
    PRODUCT_GROUP_ID("productGroupId"),
    INSERT_OPERATOR("insertOperator"),
    UPDATE_OPERATOR("updateOperator"),
    ORDER_ID("orderId"),
    PAYMENT_ID("paymentId"),
    SELLER_NAME("sellerName"),
    MEMBER_ID("memberId"),
    MEMBER_NAME("memberName"),
    BUYER_NAME("buyerName"),
    POLICY_NAME("policyName"),
    POLICY_ID("policyId"),
    DISCOUNT_TYPE("discountType"),
    CONTENT_TITLE("title"),
    CONTENT_ID("contentId"),
    BANNER_NAME("title"),
    QUESTIONER_NAME("name"),
    EXCEL_PRODUCT_GROUP_ID("productGroupId");

    private final String field;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return field;
    }

    public boolean isProductGroupId() {
        return this.equals(SearchKeyword.PRODUCT_GROUP_ID);
    }

    public boolean isProductGroupName() {
        return this.equals(SearchKeyword.PRODUCT_GROUP_NAME);
    }

    public boolean isInsertOperator() {
        return this.equals(SearchKeyword.INSERT_OPERATOR);
    }

    public boolean isUpdateOperator() {
        return this.equals(SearchKeyword.UPDATE_OPERATOR);
    }

    public boolean isExcelProductGroupId() {
        return this.equals(SearchKeyword.EXCEL_PRODUCT_GROUP_ID);
    }
}
