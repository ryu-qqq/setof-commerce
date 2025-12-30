package com.ryuqq.setof.application.review.dto.command;

import java.util.List;
import java.util.UUID;

/**
 * Create Review Command
 *
 * <p>리뷰 생성 요청 데이터를 담는 순수한 불변 객체
 *
 * @param memberId 작성자 ID (UUID)
 * @param orderId 주문 ID
 * @param productGroupId 상품 그룹 ID
 * @param rating 평점 (1-5)
 * @param content 리뷰 내용 (nullable)
 * @param imageUrls 이미지 URL 목록 (최대 3개, nullable)
 * @author development-team
 * @since 1.0.0
 */
public record CreateReviewCommand(
        UUID memberId,
        Long orderId,
        Long productGroupId,
        int rating,
        String content,
        List<String> imageUrls) {

    /** Compact Constructor - 유효성 검증 */
    public CreateReviewCommand {
        if (memberId == null) {
            throw new IllegalArgumentException("memberId는 필수입니다");
        }
        if (orderId == null) {
            throw new IllegalArgumentException("orderId는 필수입니다");
        }
        if (productGroupId == null) {
            throw new IllegalArgumentException("productGroupId는 필수입니다");
        }
        if (imageUrls != null && imageUrls.size() > 3) {
            throw new IllegalArgumentException("이미지는 최대 3개까지 등록 가능합니다");
        }
        imageUrls = imageUrls != null ? List.copyOf(imageUrls) : List.of();
    }
}
