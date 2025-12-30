package com.connectly.partnerAdmin.module.discount.dto;

import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.connectly.partnerAdmin.module.product.core.ProductGroupInfo;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("productDiscountTarget")
public class ProductDiscountTarget implements DiscountTargetResponseDto {

    private long discountPolicyId;
    private long discountTargetId;
    private long productGroupId;
    private String insertOperator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;

    @Setter
    protected ProductGroupInfo productGroup;
    @Setter
    protected Set<ProductFetchResponse> products;


    @QueryProjection
    public ProductDiscountTarget(long discountPolicyId, long discountTargetId, long productGroupId, String insertOperator, LocalDateTime insertDate) {
        this.discountPolicyId = discountPolicyId;
        this.discountTargetId = discountTargetId;
        this.productGroupId = productGroupId;
        this.insertOperator = insertOperator;
        this.insertDate = insertDate;
    }

    @Override
    public IssueType getType() {
        return IssueType.PRODUCT;
    }




}
