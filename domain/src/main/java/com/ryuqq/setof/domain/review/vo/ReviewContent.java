package com.ryuqq.setof.domain.review.vo;

/**
 * 리뷰 내용 Value Object.
 *
 * <p>리뷰 본문 텍스트를 나타내며 최대 500자 제한이 있습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ReviewContent(String value) {

    private static final int MAX_LENGTH = 500;

    public ReviewContent {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Review content must not exceed " + MAX_LENGTH + " characters");
        }
    }

    public static ReviewContent of(String value) {
        return new ReviewContent(value);
    }
}
