package com.ryuqq.setof.application.review.dto.command;

import java.util.UUID;

/**
 * Delete Review Command
 *
 * <p>리뷰 삭제 요청 데이터를 담는 순수한 불변 객체
 *
 * @param reviewId 삭제할 리뷰 ID
 * @param memberId 요청자 ID (본인 검증용, UUID)
 * @author development-team
 * @since 1.0.0
 */
public record DeleteReviewCommand(Long reviewId, UUID memberId) {

    /** Compact Constructor - 유효성 검증 */
    public DeleteReviewCommand {
        if (reviewId == null) {
            throw new IllegalArgumentException("reviewId는 필수입니다");
        }
        if (memberId == null) {
            throw new IllegalArgumentException("memberId는 필수입니다");
        }
    }
}
