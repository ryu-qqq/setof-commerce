package com.ryuqq.setof.domain.review;

import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import com.ryuqq.setof.domain.review.vo.ReviewId;
import com.ryuqq.setof.domain.review.vo.ReviewImage;
import com.ryuqq.setof.domain.review.vo.ReviewImageType;
import com.ryuqq.setof.domain.review.vo.ReviewImages;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

/**
 * Review 도메인 테스트 Fixture
 *
 * <p>테스트에서 사용되는 Review 관련 객체 생성 유틸리티
 */
public final class ReviewFixture {

    private ReviewFixture() {
        // Utility class
    }

    // ========== Default Values ==========
    public static final Long DEFAULT_REVIEW_ID = 1L;
    public static final UUID DEFAULT_MEMBER_ID =
            UUID.fromString("01929b9e-0d4f-7ab0-b4d8-1c2d3e4f5a6b");
    public static final Long DEFAULT_ORDER_ID = 200L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 300L;
    public static final int DEFAULT_RATING = 5;
    public static final String DEFAULT_CONTENT = "좋은 상품입니다. 배송도 빠르고 품질도 좋아요!";
    public static final String DEFAULT_IMAGE_URL = "https://cdn.example.com/review/image1.jpg";
    public static final Instant DEFAULT_CREATED_AT = Instant.parse("2025-01-01T00:00:00Z");
    public static final Clock FIXED_CLOCK = Clock.fixed(DEFAULT_CREATED_AT, ZoneOffset.UTC);

    // ========== ReviewId ==========

    public static ReviewId createReviewId() {
        return ReviewId.of(DEFAULT_REVIEW_ID);
    }

    public static ReviewId createReviewId(Long value) {
        return ReviewId.of(value);
    }

    // ========== Rating ==========

    public static Rating createRating() {
        return Rating.of(DEFAULT_RATING);
    }

    public static Rating createRating(int value) {
        return Rating.of(value);
    }

    public static Rating createHighRating() {
        return Rating.of(5);
    }

    public static Rating createLowRating() {
        return Rating.of(1);
    }

    // ========== ReviewContent ==========

    public static ReviewContent createContent() {
        return ReviewContent.of(DEFAULT_CONTENT);
    }

    public static ReviewContent createContent(String value) {
        return ReviewContent.of(value);
    }

    public static ReviewContent createEmptyContent() {
        return ReviewContent.empty();
    }

    // ========== ReviewImage ==========

    public static ReviewImage createImage() {
        return ReviewImage.of(ReviewImageType.PHOTO, DEFAULT_IMAGE_URL, 0);
    }

    public static ReviewImage createImage(int displayOrder) {
        return ReviewImage.of(
                ReviewImageType.PHOTO,
                "https://cdn.example.com/review/image" + displayOrder + ".jpg",
                displayOrder);
    }

    public static ReviewImage createImage(String imageUrl, int displayOrder) {
        return ReviewImage.of(ReviewImageType.PHOTO, imageUrl, displayOrder);
    }

    // ========== ReviewImages ==========

    public static ReviewImages createImages() {
        return ReviewImages.of(List.of(createImage(0), createImage(1), createImage(2)));
    }

    public static ReviewImages createImages(int count) {
        List<ReviewImage> images =
                java.util.stream.IntStream.range(0, count)
                        .mapToObj(ReviewFixture::createImage)
                        .toList();
        return ReviewImages.of(images);
    }

    public static ReviewImages createEmptyImages() {
        return ReviewImages.empty();
    }

    // ========== Review ==========

    public static Review create() {
        return Review.create(
                DEFAULT_MEMBER_ID,
                DEFAULT_ORDER_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                createRating(),
                createContent(),
                createEmptyImages(),
                FIXED_CLOCK);
    }

    public static Review create(Clock clock) {
        return Review.create(
                DEFAULT_MEMBER_ID,
                DEFAULT_ORDER_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                createRating(),
                createContent(),
                createEmptyImages(),
                clock);
    }

    public static Review createWithImages() {
        return Review.create(
                DEFAULT_MEMBER_ID,
                DEFAULT_ORDER_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                createRating(),
                createContent(),
                createImages(),
                FIXED_CLOCK);
    }

    public static Review createWithRating(int rating) {
        return Review.create(
                DEFAULT_MEMBER_ID,
                DEFAULT_ORDER_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                Rating.of(rating),
                createContent(),
                createEmptyImages(),
                FIXED_CLOCK);
    }

    public static Review reconstitute() {
        return Review.reconstitute(
                createReviewId(),
                DEFAULT_MEMBER_ID,
                DEFAULT_ORDER_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                createRating(),
                createContent(),
                createEmptyImages(),
                DEFAULT_CREATED_AT,
                null,
                null);
    }

    public static Review reconstitute(Long reviewId) {
        return Review.reconstitute(
                ReviewId.of(reviewId),
                DEFAULT_MEMBER_ID,
                DEFAULT_ORDER_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                createRating(),
                createContent(),
                createEmptyImages(),
                DEFAULT_CREATED_AT,
                null,
                null);
    }

    public static Review reconstitute(Long reviewId, UUID memberId) {
        return Review.reconstitute(
                ReviewId.of(reviewId),
                memberId,
                DEFAULT_ORDER_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                createRating(),
                createContent(),
                createEmptyImages(),
                DEFAULT_CREATED_AT,
                null,
                null);
    }

    public static Review reconstituteDeleted() {
        return Review.reconstitute(
                createReviewId(),
                DEFAULT_MEMBER_ID,
                DEFAULT_ORDER_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                createRating(),
                createContent(),
                createEmptyImages(),
                DEFAULT_CREATED_AT,
                DEFAULT_CREATED_AT.plusSeconds(3600),
                DEFAULT_CREATED_AT.plusSeconds(604800));
    }

    // ========== Builder Pattern ==========

    public static ReviewBuilder builder() {
        return new ReviewBuilder();
    }

    public static class ReviewBuilder {
        private ReviewId id = null;
        private UUID memberId = DEFAULT_MEMBER_ID;
        private Long orderId = DEFAULT_ORDER_ID;
        private Long productGroupId = DEFAULT_PRODUCT_GROUP_ID;
        private Rating rating = createRating();
        private ReviewContent content = createContent();
        private ReviewImages images = createEmptyImages();
        private Instant createdAt = DEFAULT_CREATED_AT;
        private Instant updatedAt = null;
        private Instant deletedAt = null;
        private Clock clock = FIXED_CLOCK;

        public ReviewBuilder id(Long id) {
            this.id = ReviewId.of(id);
            return this;
        }

        public ReviewBuilder memberId(UUID memberId) {
            this.memberId = memberId;
            return this;
        }

        public ReviewBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public ReviewBuilder productGroupId(Long productGroupId) {
            this.productGroupId = productGroupId;
            return this;
        }

        public ReviewBuilder rating(int rating) {
            this.rating = Rating.of(rating);
            return this;
        }

        public ReviewBuilder content(String content) {
            this.content = ReviewContent.of(content);
            return this;
        }

        public ReviewBuilder images(ReviewImages images) {
            this.images = images;
            return this;
        }

        public ReviewBuilder imagesCount(int count) {
            this.images = createImages(count);
            return this;
        }

        public ReviewBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReviewBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ReviewBuilder deleted() {
            this.deletedAt = Instant.now();
            return this;
        }

        public ReviewBuilder clock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public Review build() {
            return Review.reconstitute(
                    id,
                    memberId,
                    orderId,
                    productGroupId,
                    rating,
                    content,
                    images,
                    createdAt,
                    updatedAt,
                    deletedAt);
        }

        public Review buildNew() {
            return Review.create(memberId, orderId, productGroupId, rating, content, images, clock);
        }
    }
}
