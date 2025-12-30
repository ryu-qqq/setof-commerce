package com.ryuqq.setof.application.review.port.out.command;

import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.vo.ReviewId;

/**
 * Review Persistence Port (Command)
 *
 * <p>Review Aggregate를 영속화하는 쓰기 전용 Port
 */
public interface ReviewPersistencePort {

    /**
     * Review 저장 (신규 생성 또는 수정)
     *
     * @param review 저장할 Review (Domain Aggregate)
     * @return 저장된 Review의 ID
     */
    ReviewId persist(Review review);
}
