package com.setof.connectly.module.product.dto.price;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.discount.DiscountOffer;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroupPriceDto implements DiscountOffer {
    private long productGroupId;
    private long productId;
    private Price price;
    private long sellerId;

    @QueryProjection
    public ProductGroupPriceDto(long productGroupId, long productId, Price price, long sellerId) {
        this.productGroupId = productGroupId;
        this.productId = productId;
        this.price = price;
        this.sellerId = sellerId;
    }

    @Override
    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public void setShareRatio(double shareRatio) {}
}
