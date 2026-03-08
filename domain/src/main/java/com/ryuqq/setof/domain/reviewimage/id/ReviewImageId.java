package com.ryuqq.setof.domain.reviewimage.id;

/**
 * 리뷰 이미지 ID Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ReviewImageId(Long value) {

    public static ReviewImageId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ReviewImageId 값은 null일 수 없습니다");
        }
        return new ReviewImageId(value);
    }

    public static ReviewImageId forNew() {
        return new ReviewImageId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
