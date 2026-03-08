package com.ryuqq.setof.domain.review.vo;

/**
 * 리뷰 평점 Value Object.
 *
 * <p>평점은 1.0 이상 5.0 이하의 double 값입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record Rating(double value) {

    public Rating {
        if (value < 1.0 || value > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1.0 and 5.0");
        }
        if (value % 0.5 != 0) {
            throw new IllegalArgumentException(
                    "Rating must be in 0.5 increments (e.g. 1.0, 1.5, 2.0, ..., 5.0)");
        }
    }

    public static Rating of(double value) {
        return new Rating(value);
    }
}
