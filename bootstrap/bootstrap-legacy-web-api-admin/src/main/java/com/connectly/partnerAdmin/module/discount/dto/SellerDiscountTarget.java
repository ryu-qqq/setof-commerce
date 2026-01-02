package com.connectly.partnerAdmin.module.discount.dto;

import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("sellerDiscountTarget")
public class SellerDiscountTarget implements DiscountTargetResponseDto{

    private long discountPolicyId;
    private long discountTargetId;
    private String sellerName;
    private String insertOperator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;

    @QueryProjection
    public SellerDiscountTarget(long discountPolicyId, long discountTargetId, String sellerName, String insertOperator, LocalDateTime insertDate) {
        this.discountPolicyId = discountPolicyId;
        this.discountTargetId = discountTargetId;
        this.sellerName = sellerName;
        this.insertOperator = insertOperator;
        this.insertDate = insertDate;
    }

    @Override
    public IssueType getType() {
        return IssueType.SELLER;
    }
}
