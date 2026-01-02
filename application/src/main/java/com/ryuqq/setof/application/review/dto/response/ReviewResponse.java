package com.ryuqq.setof.application.review.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Review Response
 *
 * <p>리뷰 상세 응답 DTO
 *
 * @param id 리뷰 ID
 * @param memberId 작성자 ID (UUID)
 * @param orderId 주문 ID
 * @param productGroupId 상품 그룹 ID
 * @param rating 평점
 * @param content 리뷰 내용
 * @param images 이미지 목록
 * @param createdAt 작성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 1.0.0
 */
public record ReviewResponse(
        Long id,
        UUID memberId,
        Long orderId,
        Long productGroupId,
        int rating,
        String content,
        List<ReviewImageResponse> images,
        Instant createdAt,
        Instant updatedAt) {

    /**
     * Static Factory Method
     *
     * @param id 리뷰 ID
     * @param memberId 작성자 ID (UUID)
     * @param orderId 주문 ID
     * @param productGroupId 상품 그룹 ID
     * @param rating 평점
     * @param content 리뷰 내용
     * @param images 이미지 목록
     * @param createdAt 작성일시
     * @param updatedAt 수정일시
     * @return ReviewResponse 인스턴스
     */
    public static ReviewResponse of(
            Long id,
            UUID memberId,
            Long orderId,
            Long productGroupId,
            int rating,
            String content,
            List<ReviewImageResponse> images,
            Instant createdAt,
            Instant updatedAt) {
        return new ReviewResponse(
                id,
                memberId,
                orderId,
                productGroupId,
                rating,
                content,
                images,
                createdAt,
                updatedAt);
    }
}
