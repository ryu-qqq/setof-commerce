package com.ryuqq.setof.domain.review.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.review.ProductRatingStatsFixture;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ProductRatingStats Aggregate 테스트
 *
 * <p>상품 평점 통계 도메인 로직을 테스트합니다.
 */
@DisplayName("ProductRatingStats Aggregate")
class ProductRatingStatsTest {

    private static final Long DEFAULT_PRODUCT_GROUP_ID = 300L;

    @Nested
    @DisplayName("create() - 신규 통계 생성")
    class Create {

        @Test
        @DisplayName("신규 평점 통계를 생성할 수 있다")
        void shouldCreateNewProductRatingStats() {
            // when
            ProductRatingStats stats = ProductRatingStats.create(DEFAULT_PRODUCT_GROUP_ID);

            // then
            assertEquals(DEFAULT_PRODUCT_GROUP_ID, stats.getProductGroupId());
            assertEquals(BigDecimal.ZERO, stats.getAverageRating());
            assertEquals(0L, stats.getReviewCount());
            assertFalse(stats.hasReviews());
        }

        @Test
        @DisplayName("productGroupId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenProductGroupIdIsNull() {
            // when & then
            assertThrows(IllegalArgumentException.class, () -> ProductRatingStats.create(null));
        }

        @Test
        @DisplayName("productGroupId가 0 이하이면 예외가 발생한다")
        void shouldThrowExceptionWhenProductGroupIdIsZeroOrNegative() {
            // when & then
            assertThrows(IllegalArgumentException.class, () -> ProductRatingStats.create(0L));

            assertThrows(IllegalArgumentException.class, () -> ProductRatingStats.create(-1L));
        }
    }

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("Persistence에서 모든 필드를 복원할 수 있다")
        void shouldReconstituteFromPersistence() {
            // given
            BigDecimal averageRating = new BigDecimal("4.50");
            long reviewCount = 10L;

            // when
            ProductRatingStats stats =
                    ProductRatingStats.reconstitute(
                            DEFAULT_PRODUCT_GROUP_ID, averageRating, reviewCount);

            // then
            assertEquals(DEFAULT_PRODUCT_GROUP_ID, stats.getProductGroupId());
            assertEquals(averageRating, stats.getAverageRating());
            assertEquals(reviewCount, stats.getReviewCount());
            assertTrue(stats.hasReviews());
        }

        @Test
        @DisplayName("Fixture를 사용하여 복원할 수 있다")
        void shouldReconstituteUsingFixture() {
            // when
            ProductRatingStats stats = ProductRatingStatsFixture.reconstitute();

            // then
            assertEquals(DEFAULT_PRODUCT_GROUP_ID, stats.getProductGroupId());
            assertEquals(new BigDecimal("4.50"), stats.getAverageRating());
            assertEquals(10L, stats.getReviewCount());
        }
    }

    @Nested
    @DisplayName("addRating() - 평점 추가")
    class AddRating {

        @Test
        @DisplayName("첫 번째 평점을 추가할 수 있다")
        void shouldAddFirstRating() {
            // given
            ProductRatingStats stats = ProductRatingStats.create(DEFAULT_PRODUCT_GROUP_ID);

            // when
            stats.addRating(5);

            // then
            assertEquals(1L, stats.getReviewCount());
            assertEquals(new BigDecimal("5.00"), stats.getAverageRating());
            assertTrue(stats.hasReviews());
        }

        @Test
        @DisplayName("여러 평점을 추가하면 평균이 계산된다")
        void shouldCalculateAverageWhenMultipleRatingsAdded() {
            // given
            ProductRatingStats stats = ProductRatingStats.create(DEFAULT_PRODUCT_GROUP_ID);

            // when
            stats.addRating(5); // avg: 5.00, count: 1
            stats.addRating(4); // avg: 4.50, count: 2
            stats.addRating(3); // avg: 4.00, count: 3

            // then
            assertEquals(3L, stats.getReviewCount());
            assertEquals(new BigDecimal("4.00"), stats.getAverageRating());
        }

        @Test
        @DisplayName("평점 범위를 벗어나면 예외가 발생한다")
        void shouldThrowExceptionWhenRatingOutOfRange() {
            // given
            ProductRatingStats stats = ProductRatingStats.create(DEFAULT_PRODUCT_GROUP_ID);

            // when & then
            assertThrows(IllegalArgumentException.class, () -> stats.addRating(0));

            assertThrows(IllegalArgumentException.class, () -> stats.addRating(6));
        }

        @Test
        @DisplayName("소수점 이하 2자리까지 반올림한다")
        void shouldRoundToTwoDecimalPlaces() {
            // given
            ProductRatingStats stats = ProductRatingStats.create(DEFAULT_PRODUCT_GROUP_ID);

            // when
            stats.addRating(5);
            stats.addRating(5);
            stats.addRating(4); // (5 + 5 + 4) / 3 = 4.666... → 4.67

            // then
            assertEquals(new BigDecimal("4.67"), stats.getAverageRating());
        }
    }

    @Nested
    @DisplayName("removeRating() - 평점 제거")
    class RemoveRating {

        @Test
        @DisplayName("평점을 제거하면 평균이 재계산된다")
        void shouldRecalculateAverageWhenRatingRemoved() {
            // given
            ProductRatingStats stats =
                    ProductRatingStats.reconstitute(
                            DEFAULT_PRODUCT_GROUP_ID,
                            new BigDecimal("4.00"),
                            3L); // (5+4+3)/3 = 4.00 가정

            // when - 3점짜리 리뷰 제거
            stats.removeRating(3); // (5+4)/2 = 4.50

            // then
            assertEquals(2L, stats.getReviewCount());
            assertEquals(new BigDecimal("4.50"), stats.getAverageRating());
        }

        @Test
        @DisplayName("마지막 평점을 제거하면 0이 된다")
        void shouldResetToZeroWhenLastRatingRemoved() {
            // given
            ProductRatingStats stats = ProductRatingStatsFixture.reconstituteWithSingleReview(5);

            // when
            stats.removeRating(5);

            // then
            assertEquals(0L, stats.getReviewCount());
            assertEquals(BigDecimal.ZERO, stats.getAverageRating());
            assertFalse(stats.hasReviews());
        }

        @Test
        @DisplayName("평점 범위를 벗어나면 예외가 발생한다")
        void shouldThrowExceptionWhenRatingOutOfRange() {
            // given
            ProductRatingStats stats = ProductRatingStatsFixture.reconstitute();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> stats.removeRating(0));

            assertThrows(IllegalArgumentException.class, () -> stats.removeRating(6));
        }
    }

    @Nested
    @DisplayName("updateRating() - 평점 업데이트")
    class UpdateRating {

        @Test
        @DisplayName("평점을 업데이트하면 평균이 재계산된다")
        void shouldRecalculateAverageWhenRatingUpdated() {
            // given
            ProductRatingStats stats =
                    ProductRatingStats.reconstitute(
                            DEFAULT_PRODUCT_GROUP_ID,
                            new BigDecimal("4.00"),
                            2L); // (4+4)/2 = 4.00 가정

            // when - 4점을 5점으로 변경: (4+5)/2 = 4.50
            stats.updateRating(4, 5);

            // then
            assertEquals(2L, stats.getReviewCount()); // count는 변하지 않음
            assertEquals(new BigDecimal("4.50"), stats.getAverageRating());
        }

        @Test
        @DisplayName("같은 평점으로 업데이트해도 평균은 변하지 않는다")
        void shouldNotChangeAverageWhenSameRating() {
            // given
            ProductRatingStats stats =
                    ProductRatingStats.reconstitute(
                            DEFAULT_PRODUCT_GROUP_ID, new BigDecimal("4.00"), 2L);

            // when
            stats.updateRating(4, 4);

            // then
            assertEquals(new BigDecimal("4.00"), stats.getAverageRating());
        }

        @Test
        @DisplayName("리뷰가 없는 상태에서 업데이트하면 예외가 발생한다")
        void shouldThrowExceptionWhenNoReviews() {
            // given
            ProductRatingStats stats = ProductRatingStatsFixture.reconstituteEmpty();

            // when & then
            assertThrows(IllegalStateException.class, () -> stats.updateRating(4, 5));
        }

        @Test
        @DisplayName("평점 범위를 벗어나면 예외가 발생한다")
        void shouldThrowExceptionWhenRatingOutOfRange() {
            // given
            ProductRatingStats stats = ProductRatingStatsFixture.reconstitute();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> stats.updateRating(0, 5));

            assertThrows(IllegalArgumentException.class, () -> stats.updateRating(4, 6));
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusChecks {

        @Test
        @DisplayName("hasReviews()는 리뷰가 있으면 true를 반환한다")
        void shouldReturnTrueWhenHasReviews() {
            // given
            ProductRatingStats stats = ProductRatingStatsFixture.reconstitute();

            // then
            assertTrue(stats.hasReviews());
        }

        @Test
        @DisplayName("hasReviews()는 리뷰가 없으면 false를 반환한다")
        void shouldReturnFalseWhenNoReviews() {
            // given
            ProductRatingStats stats = ProductRatingStats.create(DEFAULT_PRODUCT_GROUP_ID);

            // then
            assertFalse(stats.hasReviews());
        }

        @Test
        @DisplayName("getAverageRatingAsDouble()은 double 값을 반환한다")
        void shouldReturnAverageAsDouble() {
            // given
            ProductRatingStats stats =
                    ProductRatingStats.reconstitute(
                            DEFAULT_PRODUCT_GROUP_ID, new BigDecimal("4.50"), 10L);

            // then
            assertEquals(4.5, stats.getAverageRatingAsDouble(), 0.001);
        }
    }

    @Nested
    @DisplayName("equals & hashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("같은 productGroupId를 가진 통계는 동등하다")
        void shouldBeEqualWhenSameProductGroupId() {
            // given
            ProductRatingStats stats1 = ProductRatingStats.create(DEFAULT_PRODUCT_GROUP_ID);
            ProductRatingStats stats2 =
                    ProductRatingStats.reconstitute(
                            DEFAULT_PRODUCT_GROUP_ID, new BigDecimal("4.50"), 10L);

            // then
            assertEquals(stats1, stats2);
            assertEquals(stats1.hashCode(), stats2.hashCode());
        }

        @Test
        @DisplayName("다른 productGroupId를 가진 통계는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentProductGroupId() {
            // given
            ProductRatingStats stats1 = ProductRatingStats.create(100L);
            ProductRatingStats stats2 = ProductRatingStats.create(200L);

            // then
            assertFalse(stats1.equals(stats2));
        }
    }
}
