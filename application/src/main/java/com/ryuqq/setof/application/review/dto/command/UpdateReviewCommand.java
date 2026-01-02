package com.ryuqq.setof.application.review.dto.command;

import java.util.List;
import java.util.UUID;

/**
 * Update Review Command
 *
 * <p>리뷰 수정 요청 데이터를 담는 순수한 불변 객체
 *
 * @param reviewId 수정할 리뷰 ID
 * @param memberId 요청자 ID (본인 검증용, UUID)
 * @param rating 새로운 평점 (nullable - null이면 변경 안함)
 * @param content 새로운 내용 (nullable - null이면 변경 안함)
 * @param imageUrls 새로운 이미지 URL 목록 (nullable - null이면 변경 안함, 최대 3개)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateReviewCommand(
        Long reviewId, UUID memberId, Integer rating, String content, List<String> imageUrls) {

    /** Compact Constructor - 유효성 검증 */
    public UpdateReviewCommand {
        if (reviewId == null) {
            throw new IllegalArgumentException("reviewId는 필수입니다");
        }
        if (memberId == null) {
            throw new IllegalArgumentException("memberId는 필수입니다");
        }
        if (imageUrls != null && imageUrls.size() > 3) {
            throw new IllegalArgumentException("이미지는 최대 3개까지 등록 가능합니다");
        }
        imageUrls = imageUrls != null ? List.copyOf(imageUrls) : null;
    }
}
