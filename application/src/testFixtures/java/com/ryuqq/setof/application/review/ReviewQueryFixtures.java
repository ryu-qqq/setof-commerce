package com.ryuqq.setof.application.review;

import com.ryuqq.setof.application.review.dto.query.AvailableReviewSearchParams;
import com.ryuqq.setof.application.review.dto.query.MyReviewSearchParams;
import com.ryuqq.setof.application.review.dto.query.ProductGroupReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.AvailableReviewOrderResult;
import com.ryuqq.setof.application.review.dto.response.AvailableReviewSliceResult;
import com.ryuqq.setof.application.review.dto.response.ReviewPageResult;
import com.ryuqq.setof.application.review.dto.response.ReviewResult;
import com.ryuqq.setof.application.review.dto.response.ReviewSliceResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.time.Instant;
import java.util.List;
import java.util.stream.LongStream;

/**
 * Review Application Query 테스트 Fixtures.
 *
 * <p>리뷰 조회 관련 SearchParams, Result 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ReviewQueryFixtures {

    private ReviewQueryFixtures() {}

    public static final long DEFAULT_USER_ID = 100L;
    public static final long DEFAULT_REVIEW_ID = 1L;
    public static final long DEFAULT_ORDER_ID = 700L;
    public static final long DEFAULT_PRODUCT_GROUP_ID = 500L;
    public static final int DEFAULT_SIZE = 20;
    public static final String DEFAULT_MEMBER_ID = "member-uuid-0001";

    // ===== AvailableReviewSearchParams =====

    public static AvailableReviewSearchParams availableReviewSearchParams() {
        return new AvailableReviewSearchParams(DEFAULT_USER_ID, null, null, DEFAULT_SIZE);
    }

    public static AvailableReviewSearchParams availableReviewSearchParams(
            Long legacyUserId, Long lastOrderId) {
        return new AvailableReviewSearchParams(legacyUserId, null, lastOrderId, DEFAULT_SIZE);
    }

    public static AvailableReviewSearchParams availableReviewSearchParams(int size) {
        return new AvailableReviewSearchParams(DEFAULT_USER_ID, null, null, size);
    }

    // ===== MyReviewSearchParams =====

    public static MyReviewSearchParams myReviewSearchParams() {
        return new MyReviewSearchParams(DEFAULT_USER_ID, null, null, DEFAULT_SIZE);
    }

    public static MyReviewSearchParams myReviewSearchParams(Long legacyUserId, Long lastReviewId) {
        return new MyReviewSearchParams(legacyUserId, null, lastReviewId, DEFAULT_SIZE);
    }

    public static MyReviewSearchParams myReviewSearchParams(int size) {
        return new MyReviewSearchParams(DEFAULT_USER_ID, null, null, size);
    }

    // ===== ProductGroupReviewSearchParams =====

    public static ProductGroupReviewSearchParams productGroupReviewSearchParams() {
        return new ProductGroupReviewSearchParams(
                DEFAULT_PRODUCT_GROUP_ID, "RECENT", 0, DEFAULT_SIZE);
    }

    public static ProductGroupReviewSearchParams productGroupReviewSearchParams(
            long productGroupId, String orderType) {
        return new ProductGroupReviewSearchParams(productGroupId, orderType, 0, DEFAULT_SIZE);
    }

    public static ProductGroupReviewSearchParams productGroupReviewSearchParams(
            int page, int size) {
        return new ProductGroupReviewSearchParams(DEFAULT_PRODUCT_GROUP_ID, "RECENT", page, size);
    }

    // ===== ReviewResult =====

    public static ReviewResult reviewResult(long reviewId) {
        return new ReviewResult(
                reviewId,
                DEFAULT_ORDER_ID,
                "order-uuid-0001",
                "홍길동",
                4.5,
                "정말 만족스러운 상품이었습니다.",
                ReviewResult.ProductGroupResult.of(
                        DEFAULT_PRODUCT_GROUP_ID,
                        "테스트 상품",
                        "https://example.com/image.jpg",
                        "사이즈:M"),
                ReviewResult.BrandResult.of(20L, "테스트 브랜드"),
                ReviewResult.CategoryResult.of(30L, "상의"),
                List.of(
                        ReviewResult.ReviewImageResult.of(
                                "REVIEW", "https://example.com/review-image.jpg")),
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-01T00:00:00Z"));
    }

    public static ReviewResult reviewResultWithoutImages(long reviewId) {
        return new ReviewResult(
                reviewId,
                DEFAULT_ORDER_ID,
                null,
                "김철수",
                3.0,
                "보통이었습니다.",
                ReviewResult.ProductGroupResult.of(
                        DEFAULT_PRODUCT_GROUP_ID,
                        "테스트 상품",
                        "https://example.com/image.jpg",
                        "사이즈:L"),
                ReviewResult.BrandResult.of(20L, "테스트 브랜드"),
                ReviewResult.CategoryResult.of(30L, "상의"),
                List.of(),
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-01T00:00:00Z"));
    }

    public static List<ReviewResult> reviewResults(int count) {
        return LongStream.rangeClosed(1, count)
                .mapToObj(ReviewQueryFixtures::reviewResult)
                .toList();
    }

    // ===== ReviewSliceResult =====

    public static ReviewSliceResult reviewSliceResult() {
        List<ReviewResult> items = reviewResults(DEFAULT_SIZE);
        return ReviewSliceResult.of(items, DEFAULT_SIZE, false, DEFAULT_REVIEW_ID, DEFAULT_SIZE);
    }

    public static ReviewSliceResult reviewSliceResultWithNext(int size) {
        List<ReviewResult> items = reviewResults(size);
        Long lastId = items.isEmpty() ? null : items.get(items.size() - 1).reviewId();
        return ReviewSliceResult.of(items, size, true, lastId, 100L);
    }

    public static ReviewSliceResult emptyReviewSliceResult() {
        return ReviewSliceResult.empty(DEFAULT_SIZE);
    }

    // ===== ReviewPageResult =====

    public static ReviewPageResult reviewPageResult() {
        List<ReviewResult> items = reviewResults(5);
        return ReviewPageResult.of(items, 0, DEFAULT_SIZE, 5L, 4.2);
    }

    public static ReviewPageResult reviewPageResult(
            int page, int size, long total, double avgRating) {
        List<ReviewResult> items = reviewResults((int) Math.min(total, size));
        return ReviewPageResult.of(items, page, size, total, avgRating);
    }

    public static ReviewPageResult emptyReviewPageResult() {
        return ReviewPageResult.empty(DEFAULT_SIZE);
    }

    // ===== AvailableReviewOrderResult =====

    public static AvailableReviewOrderResult availableReviewOrderResult(long orderId) {
        return new AvailableReviewOrderResult(
                orderId,
                "order-uuid-" + orderId,
                800L,
                "payment-uuid-0001",
                AvailableReviewOrderResult.SellerResult.of(1L, "테스트 셀러"),
                AvailableReviewOrderResult.ProductResult.of(
                        10L, DEFAULT_PRODUCT_GROUP_ID, "테스트 상품", "https://example.com/image.jpg"),
                AvailableReviewOrderResult.BrandResult.of(20L, "테스트 브랜드"),
                1,
                "DELIVERED",
                30000L,
                25000L,
                25000L,
                List.of(AvailableReviewOrderResult.OptionResult.of(100L, 200L, "사이즈", "M")),
                Instant.parse("2024-01-01T00:00:00Z"));
    }

    public static List<AvailableReviewOrderResult> availableReviewOrderResults(int count) {
        return LongStream.rangeClosed(700L, 700L + count - 1)
                .mapToObj(ReviewQueryFixtures::availableReviewOrderResult)
                .toList();
    }

    // ===== AvailableReviewSliceResult =====

    public static AvailableReviewSliceResult availableReviewSliceResult() {
        List<AvailableReviewOrderResult> items = availableReviewOrderResults(DEFAULT_SIZE);
        SliceMeta sliceMeta = SliceMeta.withCursor((Long) null, DEFAULT_SIZE, false, items.size());
        return AvailableReviewSliceResult.of(items, sliceMeta, DEFAULT_SIZE);
    }

    public static AvailableReviewSliceResult emptyAvailableReviewSliceResult() {
        return AvailableReviewSliceResult.empty();
    }
}
