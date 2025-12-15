package com.setof.connectly.module.product.entity.group.embedded;

import com.setof.connectly.module.discount.dto.DiscountCacheDto;
import com.setof.connectly.module.utils.NumberUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Price {
    @Column(name = "REGULAR_PRICE")
    private long regularPrice;

    @Column(name = "CURRENT_PRICE")
    private long currentPrice;

    @Column(name = "SALE_PRICE")
    private long salePrice;

    @Column(name = "DIRECT_DISCOUNT_RATE")
    private int directDiscountRate;

    @Column(name = "DIRECT_DISCOUNT_PRICE")
    private long directDiscountPrice;

    @Column(name = "DISCOUNT_RATE")
    private int discountRate;

    @Builder
    public Price(
            long regularPrice,
            long currentPrice,
            long salePrice,
            int directDiscountRate,
            long directDiscountPrice,
            int discountRate) {
        this.regularPrice = regularPrice;
        this.currentPrice = currentPrice;
        this.salePrice = salePrice;
        this.directDiscountRate = directDiscountRate;
        this.directDiscountPrice = directDiscountPrice;
        this.discountRate = discountRate;
    }

    public Price calculateDiscountPrice(DiscountCacheDto discountCacheDto) {
        if (discountCacheDto.isPriceType()) {
            applyPriceTypeDiscount(discountCacheDto);
        } else {
            applyRatioTypeDiscount(discountCacheDto);
        }
        return this;
    }

    private void applyPriceTypeDiscount(DiscountCacheDto discountCacheDto) {
        this.salePrice = this.currentPrice - discountCacheDto.getMaxDiscountPrice();
        this.discountRate = calculateDiscountRate(this.regularPrice, this.salePrice);
        this.directDiscountRate = calculateDiscountRate(this.currentPrice, this.salePrice);
        this.directDiscountPrice = discountCacheDto.getMaxDiscountPrice();
    }

    private void applyRatioTypeDiscount(DiscountCacheDto discountCacheDto) {
        double potentialDiscount = this.currentPrice * discountCacheDto.getDiscountRatio() * 0.01;
        if (discountCacheDto.getDiscountLimitYn().isYes()
                && discountCacheDto.getMaxDiscountPrice() <= potentialDiscount) {
            this.salePrice = this.currentPrice - discountCacheDto.getMaxDiscountPrice();
        } else {
            this.salePrice = this.currentPrice - NumberUtils.doubleToLong(potentialDiscount);
        }
        this.discountRate = calculateDiscountRate(this.regularPrice, this.salePrice);
        this.directDiscountRate = calculateDiscountRate(this.currentPrice, this.salePrice);
        this.directDiscountPrice = calculateDirectDiscountPrice(this.directDiscountRate);
    }

    private long calculateDirectDiscountPrice(long directDiscountRate) {
        double discountValue = (directDiscountRate * 0.01) * this.currentPrice;
        return Math.round(discountValue / 10.0) * 10;
    }

    private int calculateDiscountRate(long basePrice, long salePrice) {
        double discountRatio = (double) (basePrice - salePrice) / basePrice;
        return (int) Math.round(discountRatio * 100);
    }
}
