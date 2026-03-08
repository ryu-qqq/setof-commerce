package com.ryuqq.setof.domain.reviewimage.vo;

/**
 * 리뷰 이미지 정보 Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ReviewImageInfo(String imageUrl) {

    public ReviewImageInfo {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL must not be blank");
        }
    }

    public static ReviewImageInfo of(String imageUrl) {
        return new ReviewImageInfo(imageUrl);
    }
}
