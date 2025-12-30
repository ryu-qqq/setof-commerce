package com.ryuqq.setof.application.review.service.command;

import com.ryuqq.setof.application.review.dto.command.UpdateReviewCommand;
import com.ryuqq.setof.application.review.manager.command.ProductRatingStatsPersistenceManager;
import com.ryuqq.setof.application.review.manager.command.ReviewPersistenceManager;
import com.ryuqq.setof.application.review.manager.query.ProductRatingStatsReadManager;
import com.ryuqq.setof.application.review.manager.query.ReviewReadManager;
import com.ryuqq.setof.application.review.port.in.command.UpdateReviewUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import com.ryuqq.setof.domain.review.vo.ReviewImage;
import com.ryuqq.setof.domain.review.vo.ReviewImages;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리뷰 수정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>리뷰 조회 및 소유권 검증 (도메인 메서드에서 처리)
 *   <li>Review 도메인 업데이트
 *   <li>Review 저장
 *   <li>평점 변경 시 ProductRatingStats 재계산
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateReviewService implements UpdateReviewUseCase {

    private final ClockHolder clockHolder;
    private final ReviewReadManager reviewReadManager;
    private final ReviewPersistenceManager reviewPersistenceManager;
    private final ProductRatingStatsReadManager productRatingStatsReadManager;
    private final ProductRatingStatsPersistenceManager productRatingStatsPersistenceManager;

    public UpdateReviewService(
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
    public void execute(UpdateReviewCommand command) {
        Review review = reviewReadManager.findById(command.reviewId());
        int oldRating = review.getRatingValue();

        Rating newRating = command.rating() != null ? Rating.of(command.rating()) : null;
        ReviewContent newContent =
                command.content() != null ? ReviewContent.of(command.content()) : null;
        ReviewImages newImages = createImages(command.imageUrls());

        review.update(newRating, newContent, newImages, command.memberId(), clockHolder.getClock());

        reviewPersistenceManager.persist(review);

        if (isRatingChanged(command.rating(), oldRating)) {
            updateProductRatingStats(review.getProductGroupId(), oldRating, command.rating());
        }
    }

    private ReviewImages createImages(List<String> imageUrls) {
        if (imageUrls == null) {
            return null;
        }
        if (imageUrls.isEmpty()) {
            return ReviewImages.empty();
        }

        List<ReviewImage> images =
                IntStream.range(0, imageUrls.size())
                        .mapToObj(i -> ReviewImage.photo(imageUrls.get(i), i))
                        .toList();

        return ReviewImages.of(images);
    }

    private boolean isRatingChanged(Integer newRating, int oldRating) {
        return newRating != null && newRating != oldRating;
    }

    private void updateProductRatingStats(Long productGroupId, int oldRating, int newRating) {
        ProductRatingStats stats = productRatingStatsReadManager.getOrCreate(productGroupId);
        stats.updateRating(oldRating, newRating);
        productRatingStatsPersistenceManager.persist(stats);
    }
}
