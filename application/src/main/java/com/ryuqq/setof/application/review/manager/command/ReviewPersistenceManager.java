package com.ryuqq.setof.application.review.manager.command;

import com.ryuqq.setof.application.review.port.out.command.ReviewPersistencePort;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.vo.ReviewId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Review Persistence Manager
 *
 * <p>Review 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ReviewPersistenceManager {

    private final ReviewPersistencePort reviewPersistencePort;

    public ReviewPersistenceManager(ReviewPersistencePort reviewPersistencePort) {
        this.reviewPersistencePort = reviewPersistencePort;
    }

    /**
     * Review 저장
     *
     * @param review 저장할 Review
     * @return 저장된 Review의 ID
     */
    @Transactional
    public ReviewId persist(Review review) {
        return reviewPersistencePort.persist(review);
    }
}
