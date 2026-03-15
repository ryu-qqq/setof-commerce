package com.ryuqq.setof.domain.contentpage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductThumbnailSnapshot 단위 테스트")
class ProductThumbnailSnapshotTest {

    // ===== 헬퍼 =====

    /** 가격 기준으로 구분되는 스냅샷 목록 (현재가: 5000, 7000, 9000). */
    private List<ProductThumbnailSnapshot> threeAutoSnapshots() {
        return List.of(
                ContentPageFixtures.snapshotWithDetails(10L, 10000, 9000, 10),
                ContentPageFixtures.snapshotWithDetails(20L, 10000, 5000, 50),
                ContentPageFixtures.snapshotWithDetails(30L, 10000, 7000, 30));
    }

    @Nested
    @DisplayName("mergeFixedAndAuto - FIXED만 있는 경우")
    class FixedOnlyTest {

        @Test
        @DisplayName("FIXED만 있을 때 AUTO가 없으면 FIXED 그대로 반환한다")
        void fixedOnlyWithNoAuto() {
            List<ProductThumbnailSnapshot> fixed =
                    List.of(ContentPageFixtures.snapshot(1L), ContentPageFixtures.snapshot(2L));

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(
                            fixed, List.of(), OrderType.NONE, 10);

            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(1L, 2L);
        }

        @Test
        @DisplayName("FIXED 순서가 유지된다")
        void fixedOrderIsPreserved() {
            List<ProductThumbnailSnapshot> fixed =
                    List.of(
                            ContentPageFixtures.snapshot(3L),
                            ContentPageFixtures.snapshot(1L),
                            ContentPageFixtures.snapshot(2L));

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(
                            fixed, List.of(), OrderType.RECOMMEND, 10);

            assertThat(result)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(3L, 1L, 2L);
        }
    }

    @Nested
    @DisplayName("mergeFixedAndAuto - AUTO만 있는 경우")
    class AutoOnlyTest {

        @Test
        @DisplayName("AUTO만 있을 때 OrderType에 따라 정렬된다")
        void autoOnlyIsSortedByOrderType() {
            List<ProductThumbnailSnapshot> auto = threeAutoSnapshots();

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(
                            List.of(), auto, OrderType.LOW_PRICE, 10);

            // LOW_PRICE: currentPrice 오름차순 → id=20(5000), id=30(7000), id=10(9000)
            assertThat(result)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(20L, 30L, 10L);
        }

        @Test
        @DisplayName("AUTO를 HIGH_PRICE로 정렬하면 가격 내림차순이 된다")
        void autoSortedByHighPrice() {
            List<ProductThumbnailSnapshot> auto = threeAutoSnapshots();

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(
                            List.of(), auto, OrderType.HIGH_PRICE, 10);

            // HIGH_PRICE: currentPrice 내림차순 → id=10(9000), id=30(7000), id=20(5000)
            assertThat(result)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(10L, 30L, 20L);
        }
    }

    @Nested
    @DisplayName("mergeFixedAndAuto - FIXED + AUTO 병합")
    class MergeTest {

        @Test
        @DisplayName("FIXED가 AUTO보다 앞에 위치한다")
        void fixedPrecedesAuto() {
            List<ProductThumbnailSnapshot> fixed = List.of(ContentPageFixtures.snapshot(100L));
            List<ProductThumbnailSnapshot> auto =
                    List.of(ContentPageFixtures.snapshot(200L), ContentPageFixtures.snapshot(300L));

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(fixed, auto, OrderType.NONE, 10);

            assertThat(result).hasSize(3);
            assertThat(result.get(0).productGroupId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("FIXED에 있는 상품은 AUTO 목록에서 중복 제거된다")
        void autoExcludesDuplicatesFromFixed() {
            List<ProductThumbnailSnapshot> fixed =
                    List.of(ContentPageFixtures.snapshot(10L), ContentPageFixtures.snapshot(20L));
            List<ProductThumbnailSnapshot> auto =
                    List.of(
                            ContentPageFixtures.snapshot(10L), // FIXED와 중복
                            ContentPageFixtures.snapshot(30L),
                            ContentPageFixtures.snapshot(40L));

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(fixed, auto, OrderType.NONE, 10);

            // id=10은 FIXED에만 한 번 등장해야 한다
            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(10L, 20L, 30L, 40L);
        }

        @Test
        @DisplayName("FIXED와 AUTO가 모두 중복될 경우 FIXED만 유지된다")
        void allAutoAreDuplicatesOfFixed() {
            List<ProductThumbnailSnapshot> fixed =
                    List.of(ContentPageFixtures.snapshot(1L), ContentPageFixtures.snapshot(2L));
            List<ProductThumbnailSnapshot> auto =
                    List.of(ContentPageFixtures.snapshot(1L), ContentPageFixtures.snapshot(2L));

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(fixed, auto, OrderType.NONE, 10);

            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(1L, 2L);
        }
    }

    @Nested
    @DisplayName("mergeFixedAndAuto - pageSize 제한")
    class PageSizeTest {

        @Test
        @DisplayName("결과가 pageSize로 잘린다")
        void resultIsTruncatedByPageSize() {
            List<ProductThumbnailSnapshot> fixed =
                    List.of(
                            ContentPageFixtures.snapshot(1L),
                            ContentPageFixtures.snapshot(2L),
                            ContentPageFixtures.snapshot(3L));
            List<ProductThumbnailSnapshot> auto =
                    List.of(ContentPageFixtures.snapshot(4L), ContentPageFixtures.snapshot(5L));

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(fixed, auto, OrderType.NONE, 4);

            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(1L, 2L, 3L, 4L);
        }

        @Test
        @DisplayName("pageSize가 전체 개수보다 크면 전체를 반환한다")
        void pageSizeLargerThanTotalReturnsAll() {
            List<ProductThumbnailSnapshot> fixed = List.of(ContentPageFixtures.snapshot(1L));
            List<ProductThumbnailSnapshot> auto = List.of(ContentPageFixtures.snapshot(2L));

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(fixed, auto, OrderType.NONE, 100);

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("pageSize가 1이면 FIXED 첫 번째만 반환된다")
        void pageSizeOnlyReturnsFirstFixed() {
            List<ProductThumbnailSnapshot> fixed =
                    List.of(ContentPageFixtures.snapshot(1L), ContentPageFixtures.snapshot(2L));
            List<ProductThumbnailSnapshot> auto = List.of(ContentPageFixtures.snapshot(3L));

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(fixed, auto, OrderType.NONE, 1);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).productGroupId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("mergeFixedAndAuto - 빈 리스트 케이스")
    class EmptyListTest {

        @Test
        @DisplayName("FIXED와 AUTO가 모두 비어있으면 빈 리스트를 반환한다")
        void bothEmptyReturnsEmptyList() {
            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(
                            List.of(), List.of(), OrderType.NONE, 10);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("pageSize가 0이면 빈 리스트를 반환한다")
        void pageSizeZeroReturnsEmptyList() {
            List<ProductThumbnailSnapshot> fixed = List.of(ContentPageFixtures.snapshot(1L));
            List<ProductThumbnailSnapshot> auto = List.of(ContentPageFixtures.snapshot(2L));

            List<ProductThumbnailSnapshot> result =
                    ProductThumbnailSnapshot.mergeFixedAndAuto(fixed, auto, OrderType.NONE, 0);

            assertThat(result).isEmpty();
        }
    }
}
