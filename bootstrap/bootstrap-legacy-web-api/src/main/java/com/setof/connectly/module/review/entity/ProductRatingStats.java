package com.setof.connectly.module.review.entity;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "product_rating_stats")
@Entity
public class ProductRatingStats extends BaseEntity {

    @Id
    @Column(name = "PRODUCT_GROUP_ID")
    private long id;

    private double averageRating;
    private long reviewCount;

    public ProductRatingStats(long id) {
        this.id = id;
        this.averageRating = 0;
        this.reviewCount = 0;
    }

    public void updateAverageRating(double newRating) {
        if (newRating < 0 || newRating > 5) {
            throw new IllegalArgumentException("리뷰 평점은 0 과 5 점 사이여야 합니다.");
        }

        BigDecimal totalRating =
                BigDecimal.valueOf(this.averageRating)
                        .multiply(BigDecimal.valueOf(this.reviewCount));
        this.reviewCount++;
        BigDecimal newTotalRating = totalRating.add(BigDecimal.valueOf(newRating));
        BigDecimal newAverageRating =
                newTotalRating.divide(
                        BigDecimal.valueOf(this.reviewCount), 1, RoundingMode.HALF_UP);

        this.averageRating = newAverageRating.doubleValue();
    }

    public void rollBackAverageRating(double removedRating) {
        if (this.reviewCount <= 1) {
            this.averageRating = 0;
            this.reviewCount = 0;
        } else {
            BigDecimal totalRating =
                    BigDecimal.valueOf(this.averageRating)
                            .multiply(BigDecimal.valueOf(this.reviewCount))
                            .subtract(BigDecimal.valueOf(removedRating));
            this.reviewCount--;
            BigDecimal newAverageRating =
                    totalRating.divide(
                            BigDecimal.valueOf(this.reviewCount), 1, RoundingMode.HALF_UP);

            this.averageRating = newAverageRating.doubleValue();
        }
    }
}
