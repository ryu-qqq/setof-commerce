package com.setof.connectly.module.product.dto.review;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductReviewDto {
    private double averageRating;
    private long reviewCount;

    @QueryProjection
    public ProductReviewDto(double averageRating, long reviewCount) {
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

    public double getAverageRating() {
        BigDecimal bd = new BigDecimal(averageRating);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
