package com.ryuqq.setof.domain.review.id;

/**
 * 리뷰 ID Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ReviewId(Long value) {

    public static ReviewId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ReviewId 값은 null일 수 없습니다");
        }
        return new ReviewId(value);
    }

    public static ReviewId forNew() {
        return new ReviewId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
