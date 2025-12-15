package com.setof.connectly.module.discount.dto;

import com.setof.connectly.module.product.entity.group.embedded.Price;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscountCalculateDto {
    private long productGroupId;
    private long sellerId;
    private Price price;
    private double shareRatio;

    public DiscountCalculateDto(long productGroupId, long sellerId, Price price) {
        this.productGroupId = productGroupId;
        this.sellerId = sellerId;
        this.price = price;
    }

    @Builder
    public DiscountCalculateDto(
            long productGroupId, long sellerId, Price price, double shareRatio) {
        this.productGroupId = productGroupId;
        this.sellerId = sellerId;
        this.price = price;
        this.shareRatio = shareRatio;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public void setShareRatio(double shareRatio) {
        this.shareRatio = shareRatio;
    }

    public void calculateDiscountPrice(DiscountCacheDto discountCacheDto) {
        this.getPrice().calculateDiscountPrice(discountCacheDto);
    }

    @Override
    public int hashCode() {
        return (String.valueOf(productGroupId) + sellerId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiscountCalculateDto) {
            DiscountCalculateDto p = (DiscountCalculateDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }
}
