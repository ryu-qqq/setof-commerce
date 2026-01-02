package com.ryuqq.setof.application.review.service.command;

import com.ryuqq.setof.application.review.dto.command.CreateReviewCommand;
import com.ryuqq.setof.application.review.factory.command.ReviewCommandFactory;
import com.ryuqq.setof.application.review.manager.command.ProductRatingStatsPersistenceManager;
import com.ryuqq.setof.application.review.manager.command.ReviewPersistenceManager;
import com.ryuqq.setof.application.review.manager.query.ProductRatingStatsReadManager;
import com.ryuqq.setof.application.review.manager.query.ReviewReadManager;
import com.ryuqq.setof.application.review.port.in.command.CreateReviewUseCase;
import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.exception.DuplicateReviewException;
import com.ryuqq.setof.domain.review.vo.ReviewId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리뷰 생성 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>주문-상품 그룹 중복 리뷰 검증
 *   <li>ReviewCommandFactory로 Review 도메인 생성
 *   <li>Review 저장
 *   <li>ProductRatingStats 재계산 및 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateReviewService implements CreateReviewUseCase {

    private final ReviewCommandFactory reviewCommandFactory;
    private final ReviewPersistenceManager reviewPersistenceManager;
    private final ReviewReadManager reviewReadManager;
    private final ProductRatingStatsReadManager productRatingStatsReadManager;
    private final ProductRatingStatsPersistenceManager productRatingStatsPersistenceManager;

    public CreateReviewService(
            ReviewCommandFactory reviewCommandFactory,
            ReviewPersistenceManager reviewPersistenceManager,
            ReviewReadManager reviewReadManager,
            ProductRatingStatsReadManager productRatingStatsReadManager,
            ProductRatingStatsPersistenceManager productRatingStatsPersistenceManager) {
        this.reviewCommandFactory = reviewCommandFactory;
        this.reviewPersistenceManager = reviewPersistenceManager;
        this.reviewReadManager = reviewReadManager;
        this.productRatingStatsReadManager = productRatingStatsReadManager;
        this.productRatingStatsPersistenceManager = productRatingStatsPersistenceManager;
    }

    @Override
    @Transactional
    public Long execute(CreateReviewCommand command) {
        validateDuplicateReview(command);

        Review review = reviewCommandFactory.create(command);
        ReviewId reviewId = reviewPersistenceManager.persist(review);

        updateProductRatingStats(command.productGroupId(), command.rating());

        return reviewId.getValue();
    }

    private void validateDuplicateReview(CreateReviewCommand command) {
        boolean exists =
                reviewReadManager.existsByOrderIdAndProductGroupId(
                        command.orderId(), command.productGroupId());
        if (exists) {
            throw new DuplicateReviewException(
                    command.memberId(), command.orderId(), command.productGroupId());
        }
    }

    private void updateProductRatingStats(Long productGroupId, int rating) {
        ProductRatingStats stats = productRatingStatsReadManager.getOrCreate(productGroupId);
        stats.addRating(rating);
        productRatingStatsPersistenceManager.persist(stats);
    }
}
