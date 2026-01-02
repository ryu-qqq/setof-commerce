package com.ryuqq.setof.application.review.port.in.command;

import com.ryuqq.setof.application.review.dto.command.DeleteReviewCommand;

/**
 * Delete Review UseCase (Command)
 *
 * <p>리뷰 삭제를 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>리뷰 존재 여부 및 본인 검증
 *   <li>Review 도메인 소프트 삭제
 *   <li>Review 저장 (트랜잭션)
 *   <li>ProductRatingStats 재계산
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteReviewUseCase {

    /**
     * 리뷰 삭제 실행
     *
     * @param command 리뷰 삭제 명령
     */
    void execute(DeleteReviewCommand command);
}
