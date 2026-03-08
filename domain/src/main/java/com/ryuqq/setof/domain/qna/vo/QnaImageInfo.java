package com.ryuqq.setof.domain.qna.vo;

/**
 * QnaImageInfo - Q&A 이미지 정보 Value Object.
 *
 * <p>DOM-VO-001: Record + of() + Compact Constructor.
 *
 * @param imageUrl 이미지 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaImageInfo(String imageUrl) {

    public QnaImageInfo {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("이미지 URL은 필수입니다");
        }
    }

    public static QnaImageInfo of(String imageUrl) {
        return new QnaImageInfo(imageUrl);
    }
}
