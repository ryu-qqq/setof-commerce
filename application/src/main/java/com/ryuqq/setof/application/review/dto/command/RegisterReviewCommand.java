package com.ryuqq.setof.application.review.dto.command;

import java.util.List;

/**
 * RegisterReviewCommand - 리뷰 등록 커맨드.
 *
 * @param userId 레거시 회원 ID
 * @param orderId 레거시 주문 ID
 * @param productGroupId 상품그룹 ID
 * @param rating 평점
 * @param content 리뷰 내용
 * @param imageUrls 이미지 URL 목록 (프리사인드 URL로 이미 업로드됨)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RegisterReviewCommand(
        long userId,
        long orderId,
        long productGroupId,
        double rating,
        String content,
        List<String> imageUrls) {

    public boolean hasImages() {
        return imageUrls != null && !imageUrls.isEmpty();
    }
}
