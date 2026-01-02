package com.connectly.partnerAdmin.module.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {

    DISCOUNT_POLICY_ID("discountPolicyId", "id"),
    INSERT_DATE("insertDate", "insertDate"),
    UPDATE_DATE("updateDate", "updateDate"),
    BANNER_ID("bannerId", "id"),
    CONTENT_ID("contentId", "id"),
    DISPLAY_START_DATE("displayStartDate", "displayStartDate"),
    DISPLAY_END_DATE("displayEndDate", "displayEndDate"),
    PRODUCT_GROUP_ID("id", "id"),
    ORDER_ID("orderId", "id"),
    ;

    private final String displayName;
    private final String field;

    public static SortField ofDisplayName(String displayName) {
        for (SortField field : values()) {
            if (field.getDisplayName().equalsIgnoreCase(displayName)) {
                return field;
            }
        }
        return null;
    }

}
