package com.ryuqq.setof.domain.review.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * 작성 가능한 리뷰 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum AvailableReviewSortKey implements SortKey {
    ORDER_ID("orderId");

    private final String fieldName;

    AvailableReviewSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static AvailableReviewSortKey defaultKey() {
        return ORDER_ID;
    }
}
