package com.ryuqq.setof.application.qna.dto.response;

/**
 * QnA Image Response
 *
 * <p>문의 이미지 응답 DTO
 *
 * @param imageUrl 이미지 URL
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 1.0.0
 */
public record QnaImageResponse(String imageUrl, int displayOrder) {

    public static QnaImageResponse of(String imageUrl, int displayOrder) {
        return new QnaImageResponse(imageUrl, displayOrder);
    }
}
