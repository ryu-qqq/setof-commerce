package com.connectly.partnerAdmin.module.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

import com.connectly.partnerAdmin.module.discount.core.PriceHolder;
import com.connectly.partnerAdmin.module.product.core.ProductGroupInfo;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroupDetailResponse implements PriceHolder {

    protected ProductGroupInfo productGroup;

    @Setter
    protected Set<ProductFetchResponse> products;

    @QueryProjection
    public ProductGroupDetailResponse(ProductGroupInfo productGroup) {
        this.productGroup = productGroup;
    }

    @JsonIgnore
    @Override
    public Price getPrice() {
        return productGroup.getPrice();
    }

    @JsonIgnore
    @Override
    public void setPrice(Price price) {
        this.productGroup.setPrice(price);
    }

    @JsonIgnore
    @Override
    public long getProductGroupId() {
        return this.productGroup.getProductGroupId();
    }

    @JsonIgnore
    @Override
    public long getSellerId() {
        return this.productGroup.getSellerId();
    }
}
