package com.ryuqq.setof.application.review.port.in.command;

import com.ryuqq.setof.application.review.dto.command.UpdateReviewCommand;

/**
 * Update Review UseCase (Command)
 *
 * <p>리뷰 수정을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>리뷰 존재 여부 및 본인 검증
 *   <li>Review 도메인 업데이트
 *   <li>Review 저장 (트랜잭션)
 *   <li>평점 변경 시 ProductRatingStats 재계산
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateReviewUseCase {

    /**
     * 리뷰 수정 실행
     *
     * @param command 리뷰 수정 명령
     */
    void execute(UpdateReviewCommand command);
}
