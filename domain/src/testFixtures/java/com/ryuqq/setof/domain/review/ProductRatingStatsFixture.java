package com.ryuqq.setof.domain.review;

import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import java.math.BigDecimal;

/**
 * ProductRatingStats 도메인 테스트 Fixture
 *
 * <p>테스트에서 사용되는 ProductRatingStats 관련 객체 생성 유틸리티
 */
public final class ProductRatingStatsFixture {

    private ProductRatingStatsFixture() {
        // Utility class
    }

    // ========== Default Values ==========
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 300L;
    public static final BigDecimal DEFAULT_AVERAGE_RATING = new BigDecimal("4.50");
    public static final long DEFAULT_REVIEW_COUNT = 10L;

    // ========== ProductRatingStats ==========

    public static ProductRatingStats create() {
        return ProductRatingStats.create(DEFAULT_PRODUCT_GROUP_ID);
    }

    public static ProductRatingStats create(Long productGroupId) {
        return ProductRatingStats.create(productGroupId);
    }

    public static ProductRatingStats reconstitute() {
        return ProductRatingStats.reconstitute(
                DEFAULT_PRODUCT_GROUP_ID, DEFAULT_AVERAGE_RATING, DEFAULT_REVIEW_COUNT);
    }

    public static ProductRatingStats reconstitute(Long productGroupId) {
        return ProductRatingStats.reconstitute(
                productGroupId, DEFAULT_AVERAGE_RATING, DEFAULT_REVIEW_COUNT);
    }

    public static ProductRatingStats reconstitute(
            Long productGroupId, BigDecimal averageRating, long reviewCount) {
        return ProductRatingStats.reconstitute(productGroupId, averageRating, reviewCount);
    }

    public static ProductRatingStats reconstituteEmpty() {
        return ProductRatingStats.reconstitute(DEFAULT_PRODUCT_GROUP_ID, BigDecimal.ZERO, 0L);
    }

    public static ProductRatingStats reconstituteWithSingleReview(int rating) {
        return ProductRatingStats.reconstitute(
                DEFAULT_PRODUCT_GROUP_ID, new BigDecimal(rating), 1L);
    }
}
