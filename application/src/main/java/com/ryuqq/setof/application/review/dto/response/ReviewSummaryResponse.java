package com.ryuqq.setof.application.review.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * Review Summary Response
 *
 * <p>리뷰 요약 응답 DTO (목록 조회용)
 *
 * @param id 리뷰 ID
 * @param memberId 작성자 ID (UUID)
 * @param rating 평점
 * @param content 리뷰 내용 (미리보기)
 * @param hasImages 이미지 첨부 여부
 * @param imageCount 이미지 개수
 * @param createdAt 작성일시
 * @author development-team
 * @since 1.0.0
 */
public record ReviewSummaryResponse(
        Long id,
        UUID memberId,
        int rating,
        String content,
        boolean hasImages,
        int imageCount,
        Instant createdAt) {

    /**
     * Static Factory Method
     *
     * @param id 리뷰 ID
     * @param memberId 작성자 ID (UUID)
     * @param rating 평점
     * @param content 리뷰 내용
     * @param hasImages 이미지 첨부 여부
     * @param imageCount 이미지 개수
     * @param createdAt 작성일시
     * @return ReviewSummaryResponse 인스턴스
     */
    public static ReviewSummaryResponse of(
            Long id,
            UUID memberId,
            int rating,
            String content,
            boolean hasImages,
            int imageCount,
            Instant createdAt) {
        return new ReviewSummaryResponse(
                id, memberId, rating, content, hasImages, imageCount, createdAt);
    }
}
