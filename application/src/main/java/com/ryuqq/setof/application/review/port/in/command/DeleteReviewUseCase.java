package com.ryuqq.setof.application.review.port.in.command;

import com.ryuqq.setof.application.review.dto.command.DeleteReviewCommand;

/**
 * DeleteReviewUseCase - 리뷰 삭제 유즈케이스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DeleteReviewUseCase {

    /**
     * 리뷰를 삭제합니다 (소프트 삭제).
     *
     * @param command 리뷰 삭제 커맨드
     */
    void execute(DeleteReviewCommand command);
}
