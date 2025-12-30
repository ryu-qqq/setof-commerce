package com.ryuqq.setof.domain.review.vo;

import com.ryuqq.setof.domain.review.exception.InvalidReviewContentException;
import java.util.Objects;

public final class ReviewContent {

    private static final int MAX_CONTENT_LENGTH = 500;

    private final String value;

    private ReviewContent(String value) {
        validate(value);
        this.value = value;
    }

    public static ReviewContent of(String value) {
        return new ReviewContent(value);
    }

    public static ReviewContent empty() {
        return new ReviewContent("");
    }

    private void validate(String value) {
        if (value == null) {
            throw new InvalidReviewContentException("리뷰 내용은 null일 수 없습니다.");
        }
        if (value.length() > MAX_CONTENT_LENGTH) {
            throw new InvalidReviewContentException(
                    String.format(
                            "리뷰 내용은 %d자를 초과할 수 없습니다. 현재: %d자", MAX_CONTENT_LENGTH, value.length()));
        }
    }

    public String getValue() {
        return value;
    }

    public boolean isEmpty() {
        return value.isBlank();
    }

    public int length() {
        return value.length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewContent that = (ReviewContent) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ReviewContent{value='" + value + "'}";
    }
}
