package com.ryuqq.setof.application.review.service.command;

import com.ryuqq.setof.application.review.dto.command.DeleteReviewCommand;
import com.ryuqq.setof.application.review.manager.command.ProductRatingStatsPersistenceManager;
import com.ryuqq.setof.application.review.manager.command.ReviewPersistenceManager;
import com.ryuqq.setof.application.review.manager.query.ProductRatingStatsReadManager;
import com.ryuqq.setof.application.review.manager.query.ReviewReadManager;
import com.ryuqq.setof.application.review.port.in.command.DeleteReviewUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import com.ryuqq.setof.domain.review.aggregate.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리뷰 삭제 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>리뷰 조회 및 소유권 검증 (도메인 메서드에서 처리)
 *   <li>Review 도메인 소프트 삭제
 *   <li>Review 저장
 *   <li>ProductRatingStats 재계산
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteReviewService implements DeleteReviewUseCase {

    private final ClockHolder clockHolder;
    private final ReviewReadManager reviewReadManager;
    private final ReviewPersistenceManager reviewPersistenceManager;
    private final ProductRatingStatsReadManager productRatingStatsReadManager;
    private final ProductRatingStatsPersistenceManager productRatingStatsPersistenceManager;

    public DeleteReviewService(
            ClockHolder clockHolder,
            ReviewReadManager reviewReadManager,
            ReviewPersistenceManager reviewPersistenceManager,
            ProductRatingStatsReadManager productRatingStatsReadManager,
            ProductRatingStatsPersistenceManager productRatingStatsPersistenceManager) {
        this.clockHolder = clockHolder;
        this.reviewReadManager = reviewReadManager;
        this.reviewPersistenceManager = reviewPersistenceManager;
        this.productRatingStatsReadManager = productRatingStatsReadManager;
        this.productRatingStatsPersistenceManager = productRatingStatsPersistenceManager;
    }

    @Override
    @Transactional
    public void execute(DeleteReviewCommand command) {
        Review review = reviewReadManager.findById(command.reviewId());
        int rating = review.getRatingValue();
        Long productGroupId = review.getProductGroupId();

        review.delete(command.memberId(), clockHolder.getClock());

        reviewPersistenceManager.persist(review);

        updateProductRatingStats(productGroupId, rating);
    }

    private void updateProductRatingStats(Long productGroupId, int rating) {
        ProductRatingStats stats = productRatingStatsReadManager.getOrCreate(productGroupId);
        stats.removeRating(rating);
        productRatingStatsPersistenceManager.persist(stats);
    }
}
