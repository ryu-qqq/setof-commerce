package com.ryuqq.setof.domain.review;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.order.id.OrderId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.id.ReviewId;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import com.ryuqq.setof.domain.review.vo.ReviewableOrder;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import java.time.Instant;
import java.util.List;

/**
 * Review 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Review 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ReviewFixtures {

    private ReviewFixtures() {}

    // ===== ID Fixtures =====

    public static ReviewId defaultReviewId() {
        return ReviewId.of(1L);
    }

    public static ReviewId reviewId(Long value) {
        return ReviewId.of(value);
    }

    public static ReviewId newReviewId() {
        return ReviewId.forNew();
    }

    // ===== VO Fixtures =====

    public static Rating defaultRating() {
        return Rating.of(4.5);
    }

    public static Rating rating(double value) {
        return Rating.of(value);
    }

    public static Rating minRating() {
        return Rating.of(1.0);
    }

    public static Rating maxRating() {
        return Rating.of(5.0);
    }

    public static ReviewContent defaultReviewContent() {
        return ReviewContent.of("정말 만족스러운 상품이었습니다. 배송도 빠르고 품질도 좋아요.");
    }

    public static ReviewContent reviewContent(String value) {
        return ReviewContent.of(value);
    }

    public static ReviewContent emptyReviewContent() {
        return ReviewContent.of(null);
    }

    public static LegacyMemberId defaultLegacyMemberId() {
        return LegacyMemberId.of(100L);
    }

    public static MemberId defaultMemberId() {
        return MemberId.of(1L);
    }

    public static LegacyOrderId defaultLegacyOrderId() {
        return LegacyOrderId.of(700L);
    }

    public static OrderId defaultOrderId() {
        return OrderId.of("order-uuid-0001");
    }

    public static ProductGroupId defaultProductGroupId() {
        return ProductGroupId.of(500L);
    }

    // ===== Review Aggregate Fixtures =====

    public static Review newReview() {
        return Review.forNew(
                defaultLegacyMemberId(),
                defaultMemberId(),
                defaultLegacyOrderId(),
                defaultOrderId(),
                defaultProductGroupId(),
                defaultRating(),
                defaultReviewContent(),
                CommonVoFixtures.now());
    }

    public static Review newReview(Rating rating, ReviewContent content) {
        return Review.forNew(
                defaultLegacyMemberId(),
                defaultMemberId(),
                defaultLegacyOrderId(),
                defaultOrderId(),
                defaultProductGroupId(),
                rating,
                content,
                CommonVoFixtures.now());
    }

    public static Review activeReview() {
        return Review.reconstitute(
                defaultReviewId(),
                defaultLegacyMemberId(),
                defaultMemberId(),
                defaultLegacyOrderId(),
                defaultOrderId(),
                defaultProductGroupId(),
                defaultRating(),
                defaultReviewContent(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Review activeReview(Long id) {
        return Review.reconstitute(
                ReviewId.of(id),
                defaultLegacyMemberId(),
                defaultMemberId(),
                defaultLegacyOrderId(),
                defaultOrderId(),
                defaultProductGroupId(),
                defaultRating(),
                defaultReviewContent(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Review deletedReview() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return Review.reconstitute(
                ReviewId.of(2L),
                defaultLegacyMemberId(),
                defaultMemberId(),
                defaultLegacyOrderId(),
                defaultOrderId(),
                defaultProductGroupId(),
                defaultRating(),
                defaultReviewContent(),
                DeletionStatus.deletedAt(deletedAt),
                CommonVoFixtures.yesterday(),
                deletedAt);
    }

    public static Review reviewWithLegacyOnlyIds() {
        return Review.reconstitute(
                ReviewId.of(3L),
                defaultLegacyMemberId(),
                null,
                defaultLegacyOrderId(),
                null,
                defaultProductGroupId(),
                defaultRating(),
                defaultReviewContent(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== ReviewableOrder Fixtures =====

    public static ReviewableOrder defaultReviewableOrder() {
        return new ReviewableOrder(
                700L,
                "order-uuid-0001",
                800L,
                "payment-uuid-0001",
                new ReviewableOrder.SellerSnapshot(1L, "테스트 셀러"),
                new ReviewableOrder.ProductSnapshot(
                        10L, 500L, "테스트 상품", "https://example.com/image.jpg"),
                new ReviewableOrder.BrandSnapshot(20L, "테스트 브랜드"),
                1,
                "DELIVERED",
                30000L,
                25000L,
                25000L,
                List.of(ReviewableOrder.ReviewableOrderOption.of(100L, 200L, "사이즈", "M")),
                CommonVoFixtures.yesterday());
    }

    public static ReviewableOrder reviewableOrderWithOptions(
            List<ReviewableOrder.ReviewableOrderOption> options) {
        return new ReviewableOrder(
                700L,
                "order-uuid-0001",
                800L,
                "payment-uuid-0001",
                new ReviewableOrder.SellerSnapshot(1L, "테스트 셀러"),
                new ReviewableOrder.ProductSnapshot(
                        10L, 500L, "테스트 상품", "https://example.com/image.jpg"),
                new ReviewableOrder.BrandSnapshot(20L, "테스트 브랜드"),
                2,
                "DELIVERED",
                60000L,
                50000L,
                50000L,
                options,
                CommonVoFixtures.yesterday());
    }

    // ===== WrittenReview Fixtures =====

    public static WrittenReview defaultWrittenReview() {
        return new WrittenReview(
                1L,
                700L,
                "order-uuid-0001",
                "홍길동",
                4.5,
                "정말 만족스러운 상품이었습니다.",
                new WrittenReview.ProductGroupSnapshot(
                        500L, "테스트 상품", "https://example.com/image.jpg", "사이즈:M"),
                new WrittenReview.BrandSnapshot(20L, "테스트 브랜드"),
                new WrittenReview.CategorySnapshot(30L, "상의"),
                List.of(
                        new WrittenReview.WrittenReviewImage(
                                "REVIEW", "https://example.com/review-image.jpg")),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static WrittenReview writtenReviewWithoutImages() {
        return new WrittenReview(
                2L,
                701L,
                "order-uuid-0002",
                "김철수",
                3.0,
                "보통이었습니다.",
                new WrittenReview.ProductGroupSnapshot(
                        500L, "테스트 상품", "https://example.com/image.jpg", "사이즈:L"),
                new WrittenReview.BrandSnapshot(20L, "테스트 브랜드"),
                new WrittenReview.CategorySnapshot(30L, "상의"),
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }
}
