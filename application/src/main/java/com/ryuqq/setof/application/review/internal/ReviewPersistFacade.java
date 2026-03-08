package com.ryuqq.setof.application.review.internal;

import com.ryuqq.setof.application.review.dto.bundle.ReviewRegistrationBundle;
import com.ryuqq.setof.application.review.manager.ReviewCommandManager;
import com.ryuqq.setof.application.review.manager.ReviewImageCommandManager;
import com.ryuqq.setof.domain.review.id.ReviewId;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImages;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ReviewPersistFacade - 리뷰 영속 Facade.
 *
 * <p>ReviewCommandManager + ReviewImageCommandManager를 하나의 @Transactional 경계로 묶습니다. Review persist
 * 후 반환된 reviewId를 ReviewImage에 세팅합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewPersistFacade {

    private final ReviewCommandManager reviewCommandManager;
    private final ReviewImageCommandManager reviewImageCommandManager;

    public ReviewPersistFacade(
            ReviewCommandManager reviewCommandManager,
            ReviewImageCommandManager reviewImageCommandManager) {
        this.reviewCommandManager = reviewCommandManager;
        this.reviewImageCommandManager = reviewImageCommandManager;
    }

    @Transactional
    public Long persist(ReviewRegistrationBundle bundle) {
        Long reviewId = reviewCommandManager.persist(bundle.review());

        ReviewImages images = bundle.reviewImages();
        if (!images.isEmpty()) {
            ReviewImages bound = images.withReviewId(ReviewId.of(reviewId));
            reviewImageCommandManager.persistAll(bound.toList());
        }

        return reviewId;
    }
}
