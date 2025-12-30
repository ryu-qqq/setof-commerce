package com.ryuqq.setof.domain.review.aggregate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class ProductRatingStats {

    private static final int RATING_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final Long productGroupId;
    private BigDecimal averageRating;
    private long reviewCount;

    private ProductRatingStats(Long productGroupId, BigDecimal averageRating, long reviewCount) {
        this.productGroupId = productGroupId;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

    public static ProductRatingStats create(Long productGroupId) {
        validateProductGroupId(productGroupId);
        return new ProductRatingStats(productGroupId, BigDecimal.ZERO, 0L);
    }

    public static ProductRatingStats reconstitute(
            Long productGroupId, BigDecimal averageRating, long reviewCount) {
        return new ProductRatingStats(productGroupId, averageRating, reviewCount);
    }

    private static void validateProductGroupId(Long productGroupId) {
        if (productGroupId == null || productGroupId <= 0) {
            throw new IllegalArgumentException("productGroupId는 0보다 큰 값이어야 합니다.");
        }
    }

    public void addRating(int rating) {
        validateRating(rating);

        BigDecimal newRating = BigDecimal.valueOf(rating);
        BigDecimal oldTotal = averageRating.multiply(BigDecimal.valueOf(reviewCount));
        long newCount = reviewCount + 1;

        this.averageRating =
                oldTotal.add(newRating)
                        .divide(BigDecimal.valueOf(newCount), RATING_SCALE, ROUNDING_MODE);
        this.reviewCount = newCount;
    }

    public void removeRating(int rating) {
        validateRating(rating);

        if (reviewCount <= 1) {
            this.averageRating = BigDecimal.ZERO;
            this.reviewCount = 0;
            return;
        }

        BigDecimal ratingToRemove = BigDecimal.valueOf(rating);
        BigDecimal oldTotal = averageRating.multiply(BigDecimal.valueOf(reviewCount));
        long newCount = reviewCount - 1;

        this.averageRating =
                oldTotal.subtract(ratingToRemove)
                        .divide(BigDecimal.valueOf(newCount), RATING_SCALE, ROUNDING_MODE);
        this.reviewCount = newCount;
    }

    public void updateRating(int oldRating, int newRating) {
        validateRating(oldRating);
        validateRating(newRating);

        if (reviewCount == 0) {
            throw new IllegalStateException("리뷰가 없는 상태에서 평점을 업데이트할 수 없습니다.");
        }

        BigDecimal oldTotal = averageRating.multiply(BigDecimal.valueOf(reviewCount));
        BigDecimal difference = BigDecimal.valueOf(newRating - oldRating);

        this.averageRating =
                oldTotal.add(difference)
                        .divide(BigDecimal.valueOf(reviewCount), RATING_SCALE, ROUNDING_MODE);
    }

    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("평점은 1~5 사이의 값이어야 합니다.");
        }
    }

    public boolean hasReviews() {
        return reviewCount > 0;
    }

    public double getAverageRatingAsDouble() {
        return averageRating.doubleValue();
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductRatingStats that = (ProductRatingStats) o;
        return Objects.equals(productGroupId, that.productGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productGroupId);
    }

    @Override
    public String toString() {
        return "ProductRatingStats{productGroupId="
                + productGroupId
                + ", averageRating="
                + averageRating
                + ", reviewCount="
                + reviewCount
                + "}";
    }
}
