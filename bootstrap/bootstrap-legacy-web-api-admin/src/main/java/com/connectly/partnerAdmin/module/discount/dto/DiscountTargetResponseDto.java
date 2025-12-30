package com.connectly.partnerAdmin.module.discount.dto;

import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductDiscountTarget.class, name = "productDiscountTarget"),
        @JsonSubTypes.Type(value = SellerDiscountTarget.class, name = "sellerDiscountTarget")
})
public interface DiscountTargetResponseDto {
    IssueType getType();
}
