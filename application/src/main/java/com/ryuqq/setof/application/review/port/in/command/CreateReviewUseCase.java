package com.ryuqq.setof.application.review.port.in.command;

import com.ryuqq.setof.application.review.dto.command.CreateReviewCommand;

/**
 * Create Review UseCase (Command)
 *
 * <p>리뷰 생성을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>주문-상품에 대한 중복 리뷰 검증
 *   <li>Review 도메인 생성
 *   <li>Review 저장 (트랜잭션)
 *   <li>ProductRatingStats 업데이트
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateReviewUseCase {

    /**
     * 리뷰 생성 실행
     *
     * @param command 리뷰 생성 명령
     * @return 생성된 리뷰 ID
     */
    Long execute(CreateReviewCommand command);
}
