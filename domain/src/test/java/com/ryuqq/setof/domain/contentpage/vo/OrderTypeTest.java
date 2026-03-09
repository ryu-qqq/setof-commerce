package com.ryuqq.setof.domain.contentpage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OrderType 단위 테스트")
class OrderTypeTest {

    /** 정렬 검증용 스냅샷 목록 생성. productGroupId 순서를 의도적으로 섞어 정렬 효과를 확인한다. */
    private List<ProductThumbnailSnapshot> mixedSnapshots() {
        return List.of(
                // id=3: score=0.9, reviewCount=300, currentPrice=5000, discountRate=50,
                // averageRating=4.8
                ContentPageFixtures.snapshotWithDetails(3L, 10000, 5000, 50, 4.8, 300L, 0.9),
                // id=1: score=0.5, reviewCount=100, currentPrice=9000, discountRate=10,
                // averageRating=3.5
                ContentPageFixtures.snapshotWithDetails(1L, 10000, 9000, 10, 3.5, 100L, 0.5),
                // id=2: score=0.7, reviewCount=200, currentPrice=7000, discountRate=30,
                // averageRating=4.2
                ContentPageFixtures.snapshotWithDetails(2L, 10000, 7000, 30, 4.2, 200L, 0.7));
    }

    private List<ProductThumbnailSnapshot> sortWith(OrderType orderType) {
        return mixedSnapshots().stream().sorted(orderType.comparator()).toList();
    }

    @Nested
    @DisplayName("NONE - productGroupId 오름차순")
    class NoneTest {

        @Test
        @DisplayName("NONE은 productGroupId 오름차순으로 정렬한다")
        void sortByProductGroupIdAscending() {
            List<ProductThumbnailSnapshot> sorted = sortWith(OrderType.NONE);

            assertThat(sorted)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(1L, 2L, 3L);
        }
    }

    @Nested
    @DisplayName("RECOMMEND - score 내림차순")
    class RecommendTest {

        @Test
        @DisplayName("RECOMMEND는 score 내림차순으로 정렬한다")
        void sortByScoreDescending() {
            List<ProductThumbnailSnapshot> sorted = sortWith(OrderType.RECOMMEND);

            // score: 3(0.9) > 2(0.7) > 1(0.5)
            assertThat(sorted)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(3L, 2L, 1L);
        }

        @Test
        @DisplayName("RECOMMEND comparator는 score 기준 역방향 Comparator를 반환한다")
        void comparatorIsReversedByScore() {
            Comparator<ProductThumbnailSnapshot> c = OrderType.RECOMMEND.comparator();
            ProductThumbnailSnapshot high =
                    ContentPageFixtures.snapshotWithDetails(1L, 10000, 10000, 0, 4.0, 10L, 0.9);
            ProductThumbnailSnapshot low =
                    ContentPageFixtures.snapshotWithDetails(2L, 10000, 10000, 0, 4.0, 10L, 0.5);

            assertThat(c.compare(high, low)).isNegative();
            assertThat(c.compare(low, high)).isPositive();
        }
    }

    @Nested
    @DisplayName("REVIEW - reviewCount 내림차순")
    class ReviewTest {

        @Test
        @DisplayName("REVIEW는 reviewCount 내림차순으로 정렬한다")
        void sortByReviewCountDescending() {
            List<ProductThumbnailSnapshot> sorted = sortWith(OrderType.REVIEW);

            // reviewCount: 3(300) > 2(200) > 1(100)
            assertThat(sorted)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(3L, 2L, 1L);
        }
    }

    @Nested
    @DisplayName("RECENT - productGroupId 내림차순")
    class RecentTest {

        @Test
        @DisplayName("RECENT는 productGroupId 내림차순으로 정렬한다")
        void sortByProductGroupIdDescending() {
            List<ProductThumbnailSnapshot> sorted = sortWith(OrderType.RECENT);

            // id: 3 > 2 > 1
            assertThat(sorted)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(3L, 2L, 1L);
        }
    }

    @Nested
    @DisplayName("HIGH_RATING - averageRating 내림차순")
    class HighRatingTest {

        @Test
        @DisplayName("HIGH_RATING은 averageRating 내림차순으로 정렬한다")
        void sortByAverageRatingDescending() {
            List<ProductThumbnailSnapshot> sorted = sortWith(OrderType.HIGH_RATING);

            // averageRating: 3(4.8) > 2(4.2) > 1(3.5)
            assertThat(sorted)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(3L, 2L, 1L);
        }
    }

    @Nested
    @DisplayName("LOW_PRICE - currentPrice 오름차순")
    class LowPriceTest {

        @Test
        @DisplayName("LOW_PRICE는 currentPrice 오름차순으로 정렬한다")
        void sortByCurrentPriceAscending() {
            List<ProductThumbnailSnapshot> sorted = sortWith(OrderType.LOW_PRICE);

            // currentPrice: 3(5000) < 2(7000) < 1(9000)
            assertThat(sorted)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(3L, 2L, 1L);
        }
    }

    @Nested
    @DisplayName("HIGH_PRICE - currentPrice 내림차순")
    class HighPriceTest {

        @Test
        @DisplayName("HIGH_PRICE는 currentPrice 내림차순으로 정렬한다")
        void sortByCurrentPriceDescending() {
            List<ProductThumbnailSnapshot> sorted = sortWith(OrderType.HIGH_PRICE);

            // currentPrice: 1(9000) > 2(7000) > 3(5000)
            assertThat(sorted)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(1L, 2L, 3L);
        }
    }

    @Nested
    @DisplayName("LOW_DISCOUNT - discountRate 오름차순")
    class LowDiscountTest {

        @Test
        @DisplayName("LOW_DISCOUNT는 discountRate 오름차순으로 정렬한다")
        void sortByDiscountRateAscending() {
            List<ProductThumbnailSnapshot> sorted = sortWith(OrderType.LOW_DISCOUNT);

            // discountRate: 1(10) < 2(30) < 3(50)
            assertThat(sorted)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(1L, 2L, 3L);
        }
    }

    @Nested
    @DisplayName("HIGH_DISCOUNT - discountRate 내림차순")
    class HighDiscountTest {

        @Test
        @DisplayName("HIGH_DISCOUNT는 discountRate 내림차순으로 정렬한다")
        void sortByDiscountRateDescending() {
            List<ProductThumbnailSnapshot> sorted = sortWith(OrderType.HIGH_DISCOUNT);

            // discountRate: 3(50) > 2(30) > 1(10)
            assertThat(sorted)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(3L, 2L, 1L);
        }
    }

    @Nested
    @DisplayName("모든 OrderType이 Comparator를 반환한다")
    class AllOrderTypesHaveComparatorTest {

        @Test
        @DisplayName("9개 모든 OrderType에 comparator()가 null이 아닌 값을 반환한다")
        void allOrderTypesReturnNonNullComparator() {
            for (OrderType type : OrderType.values()) {
                assertThat(type.comparator())
                        .as("%s.comparator() should not be null", type)
                        .isNotNull();
            }
        }
    }
}
