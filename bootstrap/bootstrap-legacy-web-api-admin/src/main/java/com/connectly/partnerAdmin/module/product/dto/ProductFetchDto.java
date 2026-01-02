package com.connectly.partnerAdmin.module.product.dto;

import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;
import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductFetchDto {

    private long productGroupId;
    private long productId;
    private int stockQuantity;
    private ProductStatus productStatus;
    private long optionGroupId;
    private long optionDetailId;
    private OptionName optionName;
    private String optionValue;
    private BigDecimal additionalPrice;


    @QueryProjection
    public ProductFetchDto(long productGroupId, long productId, int stockQuantity, ProductStatus productStatus, long optionGroupId, long optionDetailId, OptionName optionName, String optionValue, BigDecimal additionalPrice) {
        this.productGroupId = productGroupId;
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.productStatus = productStatus;
        this.optionGroupId = optionGroupId;
        this.optionDetailId = optionDetailId;
        this.optionName = optionName;
        this.optionValue = optionValue;
        this.additionalPrice = additionalPrice;
    }

}
