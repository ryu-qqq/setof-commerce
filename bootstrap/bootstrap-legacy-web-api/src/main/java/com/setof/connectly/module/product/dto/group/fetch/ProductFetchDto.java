package com.setof.connectly.module.product.dto.group.fetch;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import com.setof.connectly.module.product.enums.option.OptionName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductFetchDto {

    private long productGroupId;
    private long productId;
    private int stockQuantity;
    private long additionalPrice;
    private ProductStatus productStatus;
    private long optionGroupId;
    private long optionDetailId;
    private OptionName optionName;
    private String optionValue;

    @QueryProjection
    public ProductFetchDto(
            long productGroupId,
            long productId,
            int stockQuantity,
            long additionalPrice,
            ProductStatus productStatus,
            long optionGroupId,
            long optionDetailId,
            OptionName optionName,
            String optionValue) {
        this.productGroupId = productGroupId;
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.additionalPrice = additionalPrice;
        this.productStatus = productStatus;
        this.optionGroupId = optionGroupId;
        this.optionDetailId = optionDetailId;
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    @Override
    public int hashCode() {
        return (productGroupId + String.valueOf(productId) + optionDetailId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductFetchDto) {
            ProductFetchDto p = (ProductFetchDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }
}
