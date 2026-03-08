package com.ryuqq.setof.domain.review.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * 상품그룹 리뷰 조회 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum ProductGroupReviewSortKey implements SortKey {
    REVIEW_ID("reviewId"),
    RATING("rating");

    private final String fieldName;

    ProductGroupReviewSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static ProductGroupReviewSortKey defaultKey() {
        return REVIEW_ID;
    }

    public static ProductGroupReviewSortKey fromOrderType(String orderType) {
        if ("HIGH_RATING".equalsIgnoreCase(orderType)) {
            return RATING;
        }
        return REVIEW_ID;
    }
}
