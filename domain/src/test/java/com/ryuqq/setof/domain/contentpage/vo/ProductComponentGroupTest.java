package com.ryuqq.setof.domain.contentpage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductComponentGroup 단위 테스트")
class ProductComponentGroupTest {

    @Nested
    @DisplayName("isEmpty() 테스트")
    class IsEmptyTest {

        @Test
        @DisplayName("nonTabComponents와 tabComponents가 모두 비어있으면 isEmpty()는 true를 반환한다")
        void emptyGroupIsEmpty() {
            ProductComponentGroup group = ContentPageFixtures.emptyGroup();

            assertThat(group.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("nonTabComponents에 항목이 있으면 isEmpty()는 false를 반환한다")
        void nonTabNotEmpty() {
            DisplayComponent comp = ContentPageFixtures.productComponent(1L, OrderType.NONE, 10);
            ProductComponentGroup group = ContentPageFixtures.nonTabOnly(List.of(comp));

            assertThat(group.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("tabComponents에 항목이 있으면 isEmpty()는 false를 반환한다")
        void tabNotEmpty() {
            DisplayTab tab = ContentPageFixtures.displayTab(10L, "탭A", 2, 3);
            DisplayComponent comp =
                    ContentPageFixtures.tabComponent(2L, OrderType.NONE, List.of(tab));
            ProductComponentGroup group = ContentPageFixtures.tabOnly(List.of(comp));

            assertThat(group.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("nonTabComponentIds() 테스트")
    class NonTabComponentIdsTest {

        @Test
        @DisplayName("nonTab 컴포넌트의 ID 목록을 반환한다")
        void returnsNonTabComponentIds() {
            DisplayComponent comp1 = ContentPageFixtures.productComponent(10L, OrderType.NONE, 5);
            DisplayComponent comp2 =
                    ContentPageFixtures.productComponent(20L, OrderType.LOW_PRICE, 5);
            ProductComponentGroup group = ContentPageFixtures.nonTabOnly(List.of(comp1, comp2));

            assertThat(group.nonTabComponentIds()).containsExactlyInAnyOrder(10L, 20L);
        }

        @Test
        @DisplayName("nonTab 컴포넌트가 없으면 빈 리스트를 반환한다")
        void emptyWhenNoNonTab() {
            ProductComponentGroup group = ContentPageFixtures.emptyGroup();

            assertThat(group.nonTabComponentIds()).isEmpty();
        }
    }

    @Nested
    @DisplayName("tabComponentIds() 테스트")
    class TabComponentIdsTest {

        @Test
        @DisplayName("tab 컴포넌트의 ID 목록을 반환한다")
        void returnsTabComponentIds() {
            DisplayTab tab = ContentPageFixtures.displayTab(100L, "탭A", 2, 2);
            DisplayComponent comp =
                    ContentPageFixtures.tabComponent(5L, OrderType.NONE, List.of(tab));
            ProductComponentGroup group = ContentPageFixtures.tabOnly(List.of(comp));

            assertThat(group.tabComponentIds()).containsExactly(5L);
        }

        @Test
        @DisplayName("tab 컴포넌트가 없으면 빈 리스트를 반환한다")
        void emptyWhenNoTab() {
            ProductComponentGroup group = ContentPageFixtures.emptyGroup();

            assertThat(group.tabComponentIds()).isEmpty();
        }
    }

    @Nested
    @DisplayName("mergeComponentProducts() 테스트 - Non-TAB 병합")
    class MergeComponentProductsTest {

        @Test
        @DisplayName("nonTabComponents가 비어있으면 빈 Map을 반환한다")
        void emptyNonTabReturnsEmptyMap() {
            ProductComponentGroup group = ContentPageFixtures.emptyGroup();

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeComponentProducts(Map.of(), Map.of());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("FIXED만 있을 때 FIXED 순서대로 반환된다")
        void onlyFixedIsReturnedInOrder() {
            DisplayComponent comp = ContentPageFixtures.productComponent(1L, OrderType.NONE, 10);
            ProductComponentGroup group = ContentPageFixtures.nonTabOnly(List.of(comp));

            List<ProductThumbnailSnapshot> fixed =
                    List.of(ContentPageFixtures.snapshot(100L), ContentPageFixtures.snapshot(200L));

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeComponentProducts(Map.of(1L, fixed), Map.of());

            assertThat(result).containsKey(1L);
            assertThat(result.get(1L))
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(100L, 200L);
        }

        @Test
        @DisplayName("AUTO만 있을 때 OrderType으로 정렬되어 반환된다")
        void onlyAutoIsSortedByOrderType() {
            // LOW_PRICE 정렬: currentPrice 오름차순
            DisplayComponent comp =
                    ContentPageFixtures.productComponent(2L, OrderType.LOW_PRICE, 10);
            ProductComponentGroup group = ContentPageFixtures.nonTabOnly(List.of(comp));

            List<ProductThumbnailSnapshot> auto =
                    List.of(
                            ContentPageFixtures.snapshotWithDetails(10L, 10000, 9000, 10),
                            ContentPageFixtures.snapshotWithDetails(20L, 10000, 5000, 50));

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeComponentProducts(Map.of(), Map.of(2L, auto));

            // LOW_PRICE: 20(5000) < 10(9000)
            assertThat(result.get(2L))
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(20L, 10L);
        }

        @Test
        @DisplayName("FIXED와 AUTO를 병합할 때 FIXED 중복이 제거된다")
        void mergeRemovesDuplicatesFromAuto() {
            DisplayComponent comp = ContentPageFixtures.productComponent(3L, OrderType.NONE, 10);
            ProductComponentGroup group = ContentPageFixtures.nonTabOnly(List.of(comp));

            List<ProductThumbnailSnapshot> fixed = List.of(ContentPageFixtures.snapshot(100L));
            List<ProductThumbnailSnapshot> auto =
                    List.of(
                            ContentPageFixtures.snapshot(100L), // 중복
                            ContentPageFixtures.snapshot(200L));

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeComponentProducts(Map.of(3L, fixed), Map.of(3L, auto));

            // 100은 FIXED에만 한 번 등장해야 한다
            assertThat(result.get(3L)).hasSize(2);
            assertThat(result.get(3L))
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(100L, 200L);
        }

        @Test
        @DisplayName("pageSize만큼 결과가 잘린다")
        void resultIsTruncatedByPageSize() {
            // pageSize=2인 컴포넌트
            DisplayComponent comp = ContentPageFixtures.productComponent(4L, OrderType.NONE, 2);
            ProductComponentGroup group = ContentPageFixtures.nonTabOnly(List.of(comp));

            List<ProductThumbnailSnapshot> fixed = List.of(ContentPageFixtures.snapshot(1L));
            List<ProductThumbnailSnapshot> auto =
                    List.of(ContentPageFixtures.snapshot(2L), ContentPageFixtures.snapshot(3L));

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeComponentProducts(Map.of(4L, fixed), Map.of(4L, auto));

            assertThat(result.get(4L)).hasSize(2);
        }

        @Test
        @DisplayName("여러 컴포넌트가 있을 때 컴포넌트별로 독립적으로 병합된다")
        void eachComponentMergedIndependently() {
            DisplayComponent comp1 = ContentPageFixtures.productComponent(10L, OrderType.NONE, 10);
            DisplayComponent comp2 = ContentPageFixtures.productComponent(20L, OrderType.NONE, 10);
            ProductComponentGroup group = ContentPageFixtures.nonTabOnly(List.of(comp1, comp2));

            Map<Long, List<ProductThumbnailSnapshot>> fixed =
                    Map.of(
                            10L, List.of(ContentPageFixtures.snapshot(100L)),
                            20L, List.of(ContentPageFixtures.snapshot(200L)));

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeComponentProducts(fixed, Map.of());

            assertThat(result).containsKeys(10L, 20L);
            assertThat(result.get(10L))
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(100L);
            assertThat(result.get(20L))
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(200L);
        }

        @Test
        @DisplayName("FIXED/AUTO Map에 해당 컴포넌트 ID가 없으면 빈 목록으로 처리된다")
        void missingMapEntryTreatedAsEmpty() {
            DisplayComponent comp = ContentPageFixtures.productComponent(99L, OrderType.NONE, 10);
            ProductComponentGroup group = ContentPageFixtures.nonTabOnly(List.of(comp));

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeComponentProducts(Map.of(), Map.of());

            assertThat(result).containsKey(99L);
            assertThat(result.get(99L)).isEmpty();
        }
    }

    @Nested
    @DisplayName("mergeTabProducts() 테스트 - TAB 병합")
    class MergeTabProductsTest {

        @Test
        @DisplayName("tabComponents가 비어있으면 빈 Map을 반환한다")
        void emptyTabReturnsEmptyMap() {
            ProductComponentGroup group = ContentPageFixtures.emptyGroup();

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeTabProducts(Map.of(), Map.of());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("탭별로 FIXED가 먼저 오고 AUTO가 뒤에 오는 순서로 병합된다")
        void tabFixedPrecedesAuto() {
            long tabId = 10L;
            DisplayTab tab = ContentPageFixtures.displayTab(tabId, "탭A", 1, 1);
            DisplayComponent comp =
                    ContentPageFixtures.tabComponent(1L, OrderType.NONE, List.of(tab));
            ProductComponentGroup group = ContentPageFixtures.tabOnly(List.of(comp));

            List<ProductThumbnailSnapshot> fixed = List.of(ContentPageFixtures.snapshot(100L));
            List<ProductThumbnailSnapshot> auto = List.of(ContentPageFixtures.snapshot(200L));

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeTabProducts(Map.of(tabId, fixed), Map.of(tabId, auto));

            assertThat(result).containsKey(tabId);
            assertThat(result.get(tabId))
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(100L, 200L);
        }

        @Test
        @DisplayName("탭 FIXED와 AUTO에 중복이 있으면 중복이 제거된다")
        void tabDuplicatesRemovedFromAuto() {
            long tabId = 20L;
            DisplayTab tab = ContentPageFixtures.displayTab(tabId, "탭B", 1, 2);
            DisplayComponent comp =
                    ContentPageFixtures.tabComponent(2L, OrderType.NONE, List.of(tab));
            ProductComponentGroup group = ContentPageFixtures.tabOnly(List.of(comp));

            List<ProductThumbnailSnapshot> fixed = List.of(ContentPageFixtures.snapshot(500L));
            List<ProductThumbnailSnapshot> auto =
                    List.of(
                            ContentPageFixtures.snapshot(500L), // 중복
                            ContentPageFixtures.snapshot(600L));

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeTabProducts(Map.of(tabId, fixed), Map.of(tabId, auto));

            assertThat(result.get(tabId)).hasSize(2);
            assertThat(result.get(tabId))
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(500L, 600L);
        }

        @Test
        @DisplayName("여러 탭이 있을 때 탭별로 독립적으로 병합된다")
        void multipleTabsMergedIndependently() {
            long tabId1 = 30L;
            long tabId2 = 40L;
            DisplayTab tab1 = ContentPageFixtures.displayTab(tabId1, "탭C", 1, 1);
            DisplayTab tab2 = ContentPageFixtures.displayTab(tabId2, "탭D", 1, 1);
            DisplayComponent comp =
                    ContentPageFixtures.tabComponent(3L, OrderType.NONE, List.of(tab1, tab2));
            ProductComponentGroup group = ContentPageFixtures.tabOnly(List.of(comp));

            Map<Long, List<ProductThumbnailSnapshot>> fixed =
                    Map.of(
                            tabId1, List.of(ContentPageFixtures.snapshot(1000L)),
                            tabId2, List.of(ContentPageFixtures.snapshot(2000L)));

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeTabProducts(fixed, Map.of());

            assertThat(result).containsKeys(tabId1, tabId2);
            assertThat(result.get(tabId1))
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(1000L);
            assertThat(result.get(tabId2))
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(2000L);
        }

        @Test
        @DisplayName("탭 Map에 해당 tabId가 없으면 빈 목록으로 처리된다")
        void missingTabEntryTreatedAsEmpty() {
            long tabId = 50L;
            DisplayTab tab = ContentPageFixtures.displayTab(tabId, "탭E", 1, 1);
            DisplayComponent comp =
                    ContentPageFixtures.tabComponent(4L, OrderType.NONE, List.of(tab));
            ProductComponentGroup group = ContentPageFixtures.tabOnly(List.of(comp));

            Map<Long, List<ProductThumbnailSnapshot>> result =
                    group.mergeTabProducts(Map.of(), Map.of());

            assertThat(result).containsKey(tabId);
            assertThat(result.get(tabId)).isEmpty();
        }
    }
}
