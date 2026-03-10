package com.ryuqq.setof.application.review.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.review.dto.response.AvailableReviewSliceResult;
import com.ryuqq.setof.application.review.dto.response.ReviewPageResult;
import com.ryuqq.setof.application.review.dto.response.ReviewResult;
import com.ryuqq.setof.application.review.dto.response.ReviewSliceResult;
import com.ryuqq.setof.domain.review.vo.ReviewableOrder;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ReviewAssembler 단위 테스트")
class ReviewAssemblerTest {

    private ReviewAssembler sut;

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    @BeforeEach
    void setUp() {
        sut = new ReviewAssembler();
    }

    private WrittenReview defaultWrittenReview(long reviewId) {
        return new WrittenReview(
                reviewId,
                700L,
                "order-uuid-001",
                "홍길동",
                4.5,
                "좋아요",
                new WrittenReview.ProductGroupSnapshot(500L, "상품", "https://img.com", "사이즈:M"),
                new WrittenReview.BrandSnapshot(20L, "브랜드"),
                new WrittenReview.CategorySnapshot(30L, "상의"),
                List.of(
                        new WrittenReview.WrittenReviewImage(
                                "REVIEW", "https://review.com/img.jpg")),
                FIXED_NOW,
                FIXED_NOW,
                FIXED_NOW);
    }

    private ReviewableOrder defaultReviewableOrder(long orderId) {
        return new ReviewableOrder(
                orderId,
                "order-uuid-" + orderId,
                800L,
                "payment-uuid-001",
                new ReviewableOrder.SellerSnapshot(1L, "셀러"),
                new ReviewableOrder.ProductSnapshot(10L, 500L, "상품", "https://img.com"),
                new ReviewableOrder.BrandSnapshot(20L, "브랜드"),
                1,
                "DELIVERED",
                30000L,
                25000L,
                25000L,
                List.of(ReviewableOrder.ReviewableOrderOption.of(100L, 200L, "사이즈", "M")),
                FIXED_NOW);
    }

    @Nested
    @DisplayName("toReviewPageResult() - WrittenReview 목록 → ReviewPageResult 변환")
    class ToReviewPageResultTest {

        @Test
        @DisplayName("WrittenReview 목록을 ReviewPageResult로 변환한다")
        void toReviewPageResult_ValidList_ReturnsReviewPageResult() {
            // given
            List<WrittenReview> reviews =
                    List.of(defaultWrittenReview(1L), defaultWrittenReview(2L));
            int page = 0;
            int size = 20;
            long totalElements = 2L;
            double averageRating = 4.5;

            // when
            ReviewPageResult result =
                    sut.toReviewPageResult(reviews, page, size, totalElements, averageRating);

            // then
            assertThat(result.results()).hasSize(2);
            assertThat(result.averageRating()).isEqualTo(averageRating);
            assertThat(result.pageMeta().totalElements()).isEqualTo(totalElements);
        }

        @Test
        @DisplayName("빈 목록으로 빈 ReviewPageResult를 생성한다")
        void toReviewPageResult_EmptyList_ReturnsEmptyPageResult() {
            // given
            List<WrittenReview> emptyList = List.of();

            // when
            ReviewPageResult result = sut.toReviewPageResult(emptyList, 0, 20, 0L, 0.0);

            // then
            assertThat(result.results()).isEmpty();
            assertThat(result.averageRating()).isZero();
            assertThat(result.pageMeta().totalElements()).isZero();
        }

        @Test
        @DisplayName("변환된 ReviewResult의 reviewId가 WrittenReview와 일치한다")
        void toReviewPageResult_ReviewIdMatchesWrittenReview() {
            // given
            WrittenReview review = defaultWrittenReview(42L);

            // when
            ReviewPageResult result = sut.toReviewPageResult(List.of(review), 0, 20, 1L, 4.5);

            // then
            assertThat(result.results().get(0).reviewId()).isEqualTo(42L);
        }

        @Test
        @DisplayName("변환된 ReviewResult의 이미지 목록이 WrittenReview와 일치한다")
        void toReviewPageResult_ImagesMatchWrittenReview() {
            // given
            WrittenReview review = defaultWrittenReview(1L);

            // when
            ReviewPageResult result = sut.toReviewPageResult(List.of(review), 0, 20, 1L, 4.5);

            // then
            List<ReviewResult.ReviewImageResult> images = result.results().get(0).reviewImages();
            assertThat(images).hasSize(1);
            assertThat(images.get(0).reviewImageType()).isEqualTo("REVIEW");
        }
    }

    @Nested
    @DisplayName("toReviewSliceResult() - WrittenReview 목록 → ReviewSliceResult 변환")
    class ToReviewSliceResultTest {

        @Test
        @DisplayName("아이템 수가 requestedSize 이하이면 hasNext=false로 SliceResult를 생성한다")
        void toReviewSliceResult_ItemsUnderSize_HasNextFalse() {
            // given
            List<WrittenReview> reviews =
                    List.of(defaultWrittenReview(1L), defaultWrittenReview(2L));
            int requestedSize = 5;
            long totalElements = 2L;

            // when
            ReviewSliceResult result =
                    sut.toReviewSliceResult(reviews, requestedSize, totalElements);

            // then
            assertThat(result.sliceMeta().hasNext()).isFalse();
            assertThat(result.results()).hasSize(2);
            assertThat(result.totalElements()).isEqualTo(totalElements);
        }

        @Test
        @DisplayName("아이템 수가 requestedSize를 초과하면 hasNext=true이고 content를 requestedSize로 자른다")
        void toReviewSliceResult_ItemsExceedSize_HasNextTrueAndContentTrimmed() {
            // given
            List<WrittenReview> reviews =
                    List.of(
                            defaultWrittenReview(1L),
                            defaultWrittenReview(2L),
                            defaultWrittenReview(3L));
            int requestedSize = 2;
            long totalElements = 50L;

            // when
            ReviewSliceResult result =
                    sut.toReviewSliceResult(reviews, requestedSize, totalElements);

            // then
            assertThat(result.sliceMeta().hasNext()).isTrue();
            assertThat(result.results()).hasSize(requestedSize);
            assertThat(result.totalElements()).isEqualTo(totalElements);
        }

        @Test
        @DisplayName("빈 목록으로 빈 SliceResult를 생성한다")
        void toReviewSliceResult_EmptyList_ReturnsEmptySliceResult() {
            // given
            List<WrittenReview> emptyList = List.of();

            // when
            ReviewSliceResult result = sut.toReviewSliceResult(emptyList, 20, 0L);

            // then
            assertThat(result.results()).isEmpty();
            assertThat(result.sliceMeta().hasNext()).isFalse();
            assertThat(result.totalElements()).isZero();
        }

        @Test
        @DisplayName("마지막 reviewId가 cursor로 설정된다")
        void toReviewSliceResult_LastReviewIdSetAsCursor() {
            // given
            List<WrittenReview> reviews =
                    List.of(defaultWrittenReview(10L), defaultWrittenReview(20L));
            int requestedSize = 5;

            // when
            ReviewSliceResult result = sut.toReviewSliceResult(reviews, requestedSize, 2L);

            // then
            assertThat(result.sliceMeta().cursor()).isEqualTo("20");
        }
    }

    @Nested
    @DisplayName(
            "toAvailableReviewSliceResult() - ReviewableOrder 목록 → AvailableReviewSliceResult 변환")
    class ToAvailableReviewSliceResultTest {

        @Test
        @DisplayName("ReviewableOrder 목록을 AvailableReviewSliceResult로 변환한다")
        void toAvailableReviewSliceResult_ValidList_ReturnsSliceResult() {
            // given
            List<ReviewableOrder> orders =
                    List.of(defaultReviewableOrder(700L), defaultReviewableOrder(701L));
            int requestedSize = 5;
            long totalElements = 2L;

            // when
            AvailableReviewSliceResult result =
                    sut.toAvailableReviewSliceResult(orders, requestedSize, totalElements);

            // then
            assertThat(result.content()).hasSize(2);
            assertThat(result.hasNext()).isFalse();
            assertThat(result.totalElements()).isEqualTo(totalElements);
        }

        @Test
        @DisplayName("아이템 수가 requestedSize를 초과하면 hasNext=true이고 content를 requestedSize로 자른다")
        void toAvailableReviewSliceResult_ItemsExceedSize_HasNextTrueAndContentTrimmed() {
            // given
            List<ReviewableOrder> orders =
                    List.of(
                            defaultReviewableOrder(700L),
                            defaultReviewableOrder(701L),
                            defaultReviewableOrder(702L));
            int requestedSize = 2;
            long totalElements = 50L;

            // when
            AvailableReviewSliceResult result =
                    sut.toAvailableReviewSliceResult(orders, requestedSize, totalElements);

            // then
            assertThat(result.content()).hasSize(requestedSize);
            assertThat(result.hasNext()).isTrue();
        }

        @Test
        @DisplayName("빈 목록으로 빈 AvailableReviewSliceResult를 생성한다")
        void toAvailableReviewSliceResult_EmptyList_ReturnsEmpty() {
            // given
            List<ReviewableOrder> emptyList = List.of();

            // when
            AvailableReviewSliceResult result = sut.toAvailableReviewSliceResult(emptyList, 20, 0L);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.hasNext()).isFalse();
            assertThat(result.totalElements()).isZero();
        }

        @Test
        @DisplayName("마지막 orderId가 cursor로 설정된다")
        void toAvailableReviewSliceResult_LastOrderIdSetAsCursor() {
            // given
            List<ReviewableOrder> orders =
                    List.of(defaultReviewableOrder(700L), defaultReviewableOrder(701L));
            int requestedSize = 5;

            // when
            AvailableReviewSliceResult result =
                    sut.toAvailableReviewSliceResult(orders, requestedSize, 2L);

            // then
            assertThat(result.sliceMeta().cursor()).isEqualTo("701");
        }
    }
}
