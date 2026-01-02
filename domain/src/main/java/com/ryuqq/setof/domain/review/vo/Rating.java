package com.ryuqq.setof.domain.review.vo;

import com.ryuqq.setof.domain.review.exception.InvalidRatingException;
import java.util.Objects;

public final class Rating {

    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    private final int value;

    private Rating(int value) {
        validate(value);
        this.value = value;
    }

    public static Rating of(int value) {
        return new Rating(value);
    }

    private void validate(int value) {
        if (value < MIN_RATING || value > MAX_RATING) {
            throw new InvalidRatingException(value);
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isHighRating() {
        return value >= 4;
    }

    public boolean isLowRating() {
        return value <= 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rating rating = (Rating) o;
        return value == rating.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Rating{value=" + value + "}";
    }
}
