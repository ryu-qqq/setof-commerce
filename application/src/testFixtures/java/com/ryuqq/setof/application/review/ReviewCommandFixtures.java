package com.ryuqq.setof.application.review;

import com.ryuqq.setof.application.review.dto.command.DeleteReviewCommand;
import com.ryuqq.setof.application.review.dto.command.RegisterReviewCommand;
import java.util.List;

/**
 * Review Application Command 테스트 Fixtures.
 *
 * <p>리뷰 등록/삭제 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ReviewCommandFixtures {

    private ReviewCommandFixtures() {}

    public static final long DEFAULT_USER_ID = 100L;
    public static final long DEFAULT_ORDER_ID = 700L;
    public static final long DEFAULT_PRODUCT_GROUP_ID = 500L;
    public static final long DEFAULT_REVIEW_ID = 1L;
    public static final double DEFAULT_RATING = 4.5;
    public static final String DEFAULT_CONTENT = "정말 만족스러운 상품이었습니다. 배송도 빠르고 품질도 좋아요.";

    // ===== RegisterReviewCommand =====

    public static RegisterReviewCommand registerCommand() {
        return new RegisterReviewCommand(
                DEFAULT_USER_ID,
                DEFAULT_ORDER_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_RATING,
                DEFAULT_CONTENT,
                List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg"));
    }

    public static RegisterReviewCommand registerCommandWithoutImages() {
        return new RegisterReviewCommand(
                DEFAULT_USER_ID,
                DEFAULT_ORDER_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_RATING,
                DEFAULT_CONTENT,
                List.of());
    }

    public static RegisterReviewCommand registerCommand(
            long userId, long orderId, long productGroupId, double rating, String content) {
        return new RegisterReviewCommand(
                userId, orderId, productGroupId, rating, content, List.of());
    }

    public static RegisterReviewCommand registerCommand(long userId, long orderId) {
        return new RegisterReviewCommand(
                userId,
                orderId,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_RATING,
                DEFAULT_CONTENT,
                List.of());
    }

    // ===== DeleteReviewCommand =====

    public static DeleteReviewCommand deleteCommand() {
        return new DeleteReviewCommand(DEFAULT_REVIEW_ID, DEFAULT_USER_ID);
    }

    public static DeleteReviewCommand deleteCommand(long reviewId, long userId) {
        return new DeleteReviewCommand(reviewId, userId);
    }

    public static DeleteReviewCommand deleteCommand(long reviewId) {
        return new DeleteReviewCommand(reviewId, DEFAULT_USER_ID);
    }
}
