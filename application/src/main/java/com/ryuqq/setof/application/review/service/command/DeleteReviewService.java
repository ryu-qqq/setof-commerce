package com.ryuqq.setof.application.review.service.command;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.review.dto.command.DeleteReviewCommand;
import com.ryuqq.setof.application.review.factory.ReviewCommandFactory;
import com.ryuqq.setof.application.review.manager.ReviewCommandManager;
import com.ryuqq.setof.application.review.manager.ReviewImageCommandManager;
import com.ryuqq.setof.application.review.manager.ReviewImageReadManager;
import com.ryuqq.setof.application.review.port.in.command.DeleteReviewUseCase;
import com.ryuqq.setof.application.review.validator.ReviewDeletionValidator;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.id.ReviewId;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DeleteReviewService - 리뷰 삭제 Service.
 *
 * <p>Validator로 리뷰 조회 → ReadManager로 이미지 조회 → Factory(UpdateContext) → 리뷰/이미지 소프트 삭제 → persist.
 *
 * <p>TODO: 주문 상태 업데이트 (order.deleteReview → reviewYn = 'N') - 추후 구현
 *
 * <p>TODO: 상품 평점 통계 롤백 (ProductRatingStats.rollBackAverageRating) - 추후 구현
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class DeleteReviewService implements DeleteReviewUseCase {

    private final ReviewDeletionValidator validator;
    private final ReviewImageReadManager reviewImageReadManager;
    private final ReviewCommandFactory factory;
    private final ReviewCommandManager reviewCommandManager;
    private final ReviewImageCommandManager reviewImageCommandManager;

    public DeleteReviewService(
            ReviewDeletionValidator validator,
            ReviewImageReadManager reviewImageReadManager,
            ReviewCommandFactory factory,
            ReviewCommandManager reviewCommandManager,
            ReviewImageCommandManager reviewImageCommandManager) {
        this.validator = validator;
        this.reviewImageReadManager = reviewImageReadManager;
        this.factory = factory;
        this.reviewCommandManager = reviewCommandManager;
        this.reviewImageCommandManager = reviewImageCommandManager;
    }

    @Override
    @Transactional
    public void execute(DeleteReviewCommand command) {
        Review review = validator.getExistingReview(command.reviewId(), command.userId());

        UpdateContext<ReviewId, DeletionStatus> context = factory.createDeleteContext(command);

        review.delete(context.changedAt());
        reviewCommandManager.persist(review);

        ReviewImages images = reviewImageReadManager.fetchByReviewId(command.reviewId());
        if (!images.isEmpty()) {
            images.deleteAll(context.changedAt());
            reviewImageCommandManager.persistAll(images.toList());
        }

        // TODO: 주문 상태 업데이트 (order.reviewYn = 'N')
        // TODO: 상품 평점 통계 롤백 (ProductRatingStats.rollBackAverageRating)
    }
}
