package com.connectly.partnerAdmin.module.product.entity.group.embedded;

import com.connectly.partnerAdmin.module.generic.money.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Embeddable
public class Price {

    
    @Column(name = "REGULAR_PRICE")
    private BigDecimal regularPrice;

    
    @Column(name = "CURRENT_PRICE")
    private BigDecimal currentPrice;

    @Setter
    @Column(name = "SALE_PRICE")
    private BigDecimal salePrice;

    @Setter
    @Column(name = "DIRECT_DISCOUNT_PRICE")
    private BigDecimal directDiscountPrice;

    @Setter
    @Column(name = "DIRECT_DISCOUNT_RATE")
    private int directDiscountRate;

    @Setter
    @Column(name = "DISCOUNT_RATE")
    private int discountRate;

    public Price(Money regularPrice, Money currentPrice) {
        this.regularPrice = regularPrice.getAmount();
        this.currentPrice = currentPrice.getAmount();
        this.salePrice = currentPrice.getAmount();
        this.discountRate = calculateDiscountRate(regularPrice, currentPrice);
        this.directDiscountPrice = Money.ZERO.getAmount();  // 초기값 설정
        this.directDiscountRate = 0;
    }

    private int calculateDiscountRate(Money basePrice, Money salePrice) {
        if (basePrice.isLessThan(Money.wons(1))) {
            throw new IllegalArgumentException("Base price cannot be zero.");
        }

        Money discount = basePrice.minus(salePrice);
        BigDecimal discountRate = discount.divide(basePrice, 2, RoundingMode.HALF_UP); // Money 객체의 divide 메서드 사용
        return discountRate.multiply(BigDecimal.valueOf(100)).intValueExact();
    }


    @Override
    public String toString() {
        return String.format("regularPrice: %s, currentPrice: %s, salePrice: %s", regularPrice, currentPrice, salePrice);
    }
}