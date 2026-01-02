package com.ryuqq.setof.domain.review.vo;

import java.util.Objects;

public final class ReviewImage {

    private final ReviewImageType imageType;
    private final String imageUrl;
    private final int displayOrder;

    private ReviewImage(ReviewImageType imageType, String imageUrl, int displayOrder) {
        validateImageType(imageType);
        validateImageUrl(imageUrl);
        validateDisplayOrder(displayOrder);
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
    }

    public static ReviewImage of(ReviewImageType imageType, String imageUrl, int displayOrder) {
        return new ReviewImage(imageType, imageUrl, displayOrder);
    }

    public static ReviewImage photo(String imageUrl, int displayOrder) {
        return new ReviewImage(ReviewImageType.PHOTO, imageUrl, displayOrder);
    }

    private void validateImageType(ReviewImageType imageType) {
        if (imageType == null) {
            throw new IllegalArgumentException("이미지 타입은 필수입니다.");
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("이미지 URL은 필수입니다.");
        }
    }

    private void validateDisplayOrder(int displayOrder) {
        if (displayOrder < 0) {
            throw new IllegalArgumentException("표시 순서는 0 이상이어야 합니다.");
        }
    }

    public ReviewImageType getImageType() {
        return imageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public boolean isPhoto() {
        return imageType.isPhoto();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewImage that = (ReviewImage) o;
        return displayOrder == that.displayOrder
                && imageType == that.imageType
                && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageType, imageUrl, displayOrder);
    }

    @Override
    public String toString() {
        return "ReviewImage{imageType="
                + imageType
                + ", imageUrl='"
                + imageUrl
                + "', displayOrder="
                + displayOrder
                + "}";
    }
}
