package com.ryuqq.setof.application.review.factory;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.review.dto.bundle.ReviewRegistrationBundle;
import com.ryuqq.setof.application.review.dto.command.DeleteReviewCommand;
import com.ryuqq.setof.application.review.dto.command.RegisterReviewCommand;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.id.ReviewId;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImage;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImages;
import com.ryuqq.setof.domain.reviewimage.vo.ReviewImageInfo;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * ReviewCommandFactory - 리뷰 도메인 객체 생성 Factory.
 *
 * <p>Command DTO를 Bundle(Review + ReviewImages)로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewCommandFactory {

    /**
     * 리뷰 등록 번들 생성.
     *
     * @param command 리뷰 등록 커맨드
     * @return ReviewRegistrationBundle (Review + ReviewImages)
     */
    public ReviewRegistrationBundle createRegistrationBundle(RegisterReviewCommand command) {
        Instant now = Instant.now();

        Review review =
                Review.forNew(
                        LegacyMemberId.of(command.userId()),
                        null,
                        LegacyOrderId.of(command.orderId()),
                        null,
                        ProductGroupId.of(command.productGroupId()),
                        Rating.of(command.rating()),
                        ReviewContent.of(command.content()),
                        now);

        ReviewImages images = createImages(command.imageUrls());

        return new ReviewRegistrationBundle(review, images);
    }

    /**
     * 리뷰 삭제 UpdateContext 생성.
     *
     * @param command 리뷰 삭제 커맨드
     * @return UpdateContext (ReviewId, DeletionStatus, changedAt)
     */
    public UpdateContext<ReviewId, DeletionStatus> createDeleteContext(
            DeleteReviewCommand command) {
        Instant now = Instant.now();
        return new UpdateContext<>(
                ReviewId.of(command.reviewId()), DeletionStatus.deletedAt(now), now);
    }

    private ReviewImages createImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return ReviewImages.empty();
        }

        AtomicInteger order = new AtomicInteger(0);
        Instant now = Instant.now();

        List<ReviewImage> images =
                imageUrls.stream()
                        .map(
                                url ->
                                        ReviewImage.create(
                                                null,
                                                ReviewImageInfo.of(url),
                                                order.getAndIncrement(),
                                                now))
                        .toList();

        return ReviewImages.of(images);
    }
}
