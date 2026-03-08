package com.ryuqq.setof.domain.review.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * 내 리뷰 조회 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum MyReviewSortKey implements SortKey {
    REVIEW_ID("reviewId");

    private final String fieldName;

    MyReviewSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static MyReviewSortKey defaultKey() {
        return REVIEW_ID;
    }
}
