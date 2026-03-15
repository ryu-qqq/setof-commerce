package com.ryuqq.setof.domain.contentpage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ComponentProductBundle Value Object 단위 테스트")
class ComponentProductBundleTest {

    @Nested
    @DisplayName("empty() - 빈 번들 생성")
    class EmptyTest {

        @Test
        @DisplayName("empty()로 생성된 번들은 componentProducts가 비어있다")
        void emptyBundleHasEmptyComponentProducts() {
            // when
            ComponentProductBundle bundle = ComponentProductBundle.empty();

            // then
            assertThat(bundle.componentProducts()).isEmpty();
        }

        @Test
        @DisplayName("empty()로 생성된 번들은 tabProducts가 비어있다")
        void emptyBundleHasEmptyTabProducts() {
            // when
            ComponentProductBundle bundle = ComponentProductBundle.empty();

            // then
            assertThat(bundle.tabProducts()).isEmpty();
        }
    }

    @Nested
    @DisplayName("forComponent() - 컴포넌트 ID로 상품 조회")
    class ForComponentTest {

        @Test
        @DisplayName("존재하는 componentId로 조회하면 해당 목록을 반환한다")
        void returnsProductsForExistingComponentId() {
            // given
            long componentId = 1L;
            List<ProductThumbnailSnapshot> snapshots =
                    List.of(ContentPageFixtures.snapshot(100L), ContentPageFixtures.snapshot(200L));
            ComponentProductBundle bundle =
                    new ComponentProductBundle(Map.of(componentId, snapshots), Map.of());

            // when
            List<ProductThumbnailSnapshot> result = bundle.forComponent(componentId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(ProductThumbnailSnapshot::productGroupId)
                    .containsExactly(100L, 200L);
        }

        @Test
        @DisplayName("존재하지 않는 componentId로 조회하면 빈 리스트를 반환한다")
        void returnsEmptyListForMissingComponentId() {
            // given
            ComponentProductBundle bundle =
                    new ComponentProductBundle(Map.of(1L, List.of()), Map.of());

            // when
            List<ProductThumbnailSnapshot> result = bundle.forComponent(999L);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("empty() 번들에서 forComponent()를 호출하면 빈 리스트를 반환한다")
        void emptyBundleForComponentReturnsEmptyList() {
            // given
            ComponentProductBundle bundle = ComponentProductBundle.empty();

            // when
            List<ProductThumbnailSnapshot> result = bundle.forComponent(1L);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("forTab() - 탭 ID로 상품 조회")
    class ForTabTest {

        @Test
        @DisplayName("존재하는 tabId로 조회하면 해당 목록을 반환한다")
        void returnsProductsForExistingTabId() {
            // given
            long tabId = 10L;
            List<ProductThumbnailSnapshot> snapshots = List.of(ContentPageFixtures.snapshot(300L));
            ComponentProductBundle bundle =
                    new ComponentProductBundle(Map.of(), Map.of(tabId, snapshots));

            // when
            List<ProductThumbnailSnapshot> result = bundle.forTab(tabId);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).productGroupId()).isEqualTo(300L);
        }

        @Test
        @DisplayName("존재하지 않는 tabId로 조회하면 빈 리스트를 반환한다")
        void returnsEmptyListForMissingTabId() {
            // given
            ComponentProductBundle bundle =
                    new ComponentProductBundle(Map.of(), Map.of(10L, List.of()));

            // when
            List<ProductThumbnailSnapshot> result = bundle.forTab(999L);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("empty() 번들에서 forTab()을 호출하면 빈 리스트를 반환한다")
        void emptyBundleForTabReturnsEmptyList() {
            // given
            ComponentProductBundle bundle = ComponentProductBundle.empty();

            // when
            List<ProductThumbnailSnapshot> result = bundle.forTab(10L);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("componentProducts와 tabProducts가 독립적으로 관리된다")
    class IndependenceTest {

        @Test
        @DisplayName("componentId와 tabId가 동일한 값이어도 독립적으로 조회된다")
        void componentAndTabWithSameIdAreIndependent() {
            // given
            long sameId = 5L;
            List<ProductThumbnailSnapshot> componentSnapshots =
                    List.of(ContentPageFixtures.snapshot(100L));
            List<ProductThumbnailSnapshot> tabSnapshots =
                    List.of(ContentPageFixtures.snapshot(200L));

            ComponentProductBundle bundle =
                    new ComponentProductBundle(
                            Map.of(sameId, componentSnapshots), Map.of(sameId, tabSnapshots));

            // when
            List<ProductThumbnailSnapshot> fromComponent = bundle.forComponent(sameId);
            List<ProductThumbnailSnapshot> fromTab = bundle.forTab(sameId);

            // then
            assertThat(fromComponent.get(0).productGroupId()).isEqualTo(100L);
            assertThat(fromTab.get(0).productGroupId()).isEqualTo(200L);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 내용의 번들은 동등하다")
        void bundlesWithSameContentAreEqual() {
            // given
            Map<Long, List<ProductThumbnailSnapshot>> componentMap =
                    Map.of(1L, List.of(ContentPageFixtures.snapshot(100L)));
            Map<Long, List<ProductThumbnailSnapshot>> tabMap =
                    Map.of(10L, List.of(ContentPageFixtures.snapshot(200L)));

            ComponentProductBundle bundle1 = new ComponentProductBundle(componentMap, tabMap);
            ComponentProductBundle bundle2 = new ComponentProductBundle(componentMap, tabMap);

            // then
            assertThat(bundle1).isEqualTo(bundle2);
        }
    }
}
