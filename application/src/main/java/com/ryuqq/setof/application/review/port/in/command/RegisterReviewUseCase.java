package com.ryuqq.setof.application.review.port.in.command;

import com.ryuqq.setof.application.review.dto.command.RegisterReviewCommand;

/**
 * RegisterReviewUseCase - 리뷰 등록 유즈케이스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RegisterReviewUseCase {

    /**
     * 리뷰를 등록합니다.
     *
     * @param command 리뷰 등록 커맨드
     * @return 등록된 리뷰 ID
     */
    Long execute(RegisterReviewCommand command);
}
