package com.ryuqq.setof.domain.qna.vo;

import java.util.Objects;

public final class QnaImage {

    private final String imageUrl;
    private final int displayOrder;

    private QnaImage(String imageUrl, int displayOrder) {
        validateImageUrl(imageUrl);
        validateDisplayOrder(displayOrder);
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
    }

    public static QnaImage of(String imageUrl, int displayOrder) {
        return new QnaImage(imageUrl, displayOrder);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QnaImage qnaImage = (QnaImage) o;
        return displayOrder == qnaImage.displayOrder && Objects.equals(imageUrl, qnaImage.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl, displayOrder);
    }

    @Override
    public String toString() {
        return "QnaImage{imageUrl='" + imageUrl + "', displayOrder=" + displayOrder + "}";
    }
}
