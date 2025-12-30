package com.ryuqq.setof.application.review.dto.response;

/**
 * Review Image Response
 *
 * <p>리뷰 이미지 응답 DTO
 *
 * @param imageUrl 이미지 URL
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 1.0.0
 */
public record ReviewImageResponse(String imageUrl, int displayOrder) {

    /**
     * Static Factory Method
     *
     * @param imageUrl 이미지 URL
     * @param displayOrder 표시 순서
     * @return ReviewImageResponse 인스턴스
     */
    public static ReviewImageResponse of(String imageUrl, int displayOrder) {
        return new ReviewImageResponse(imageUrl, displayOrder);
    }
}
