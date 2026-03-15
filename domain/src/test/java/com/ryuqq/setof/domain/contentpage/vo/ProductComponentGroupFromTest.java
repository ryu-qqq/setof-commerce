package com.ryuqq.setof.domain.contentpage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductComponentGroup.from() 팩토리 메서드 단위 테스트")
class ProductComponentGroupFromTest {

    @Nested
    @DisplayName("from() - 컴포넌트 목록에서 상품 컴포넌트 분류")
    class FromTest {

        @Test
        @DisplayName("빈 리스트로 생성하면 nonTab과 tab 모두 비어있다")
        void emptyListResultsInEmptyGroup() {
            // when
            ProductComponentGroup group = ProductComponentGroup.from(List.of());

            // then
            assertThat(group.isEmpty()).isTrue();
            assertThat(group.nonTabComponents()).isEmpty();
            assertThat(group.tabComponents()).isEmpty();
        }

        @Test
        @DisplayName("TEXT 타입 컴포넌트만 있으면 상품 컴포넌트가 없다")
        void textOnlyComponentsResultInEmptyGroup() {
            // given
            DisplayComponent textComp = ContentPageFixtures.textComponent(1L);

            // when
            ProductComponentGroup group = ProductComponentGroup.from(List.of(textComp));

            // then
            assertThat(group.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("PRODUCT 타입 컴포넌트는 nonTabComponents에 분류된다")
        void productComponentGoesIntoNonTab() {
            // given
            DisplayComponent productComp =
                    ContentPageFixtures.productComponent(1L, OrderType.NONE, 10);

            // when
            ProductComponentGroup group = ProductComponentGroup.from(List.of(productComp));

            // then
            assertThat(group.nonTabComponents()).hasSize(1);
            assertThat(group.tabComponents()).isEmpty();
        }

        @Test
        @DisplayName("TAB 타입 컴포넌트는 tabComponents에 분류된다")
        void tabComponentGoesIntoTab() {
            // given
            DisplayTab tab = ContentPageFixtures.displayTab(10L, "탭A", 1, 1);
            DisplayComponent tabComp =
                    ContentPageFixtures.tabComponent(2L, OrderType.NONE, List.of(tab));

            // when
            ProductComponentGroup group = ProductComponentGroup.from(List.of(tabComp));

            // then
            assertThat(group.nonTabComponents()).isEmpty();
            assertThat(group.tabComponents()).hasSize(1);
        }

        @Test
        @DisplayName("PRODUCT와 TAB이 혼합된 경우 각각 올바른 그룹에 분류된다")
        void mixedComponentsAreClassifiedCorrectly() {
            // given
            DisplayComponent productComp =
                    ContentPageFixtures.productComponent(1L, OrderType.NONE, 10);
            DisplayComponent textComp = ContentPageFixtures.textComponent(2L);
            DisplayTab tab = ContentPageFixtures.displayTab(10L, "탭A", 0, 0);
            DisplayComponent tabComp =
                    ContentPageFixtures.tabComponent(3L, OrderType.NONE, List.of(tab));

            // when
            ProductComponentGroup group =
                    ProductComponentGroup.from(List.of(productComp, textComp, tabComp));

            // then
            assertThat(group.nonTabComponents()).hasSize(1);
            assertThat(group.tabComponents()).hasSize(1);
            assertThat(group.nonTabComponentIds()).containsExactly(1L);
            assertThat(group.tabComponentIds()).containsExactly(3L);
        }

        @Test
        @DisplayName("TEXT 컴포넌트는 상품 컴포넌트 그룹에서 제외된다")
        void textComponentsAreFilteredOut() {
            // given
            DisplayComponent textComp1 = ContentPageFixtures.textComponent(1L);
            DisplayComponent textComp2 = ContentPageFixtures.textComponent(2L);
            DisplayComponent productComp =
                    ContentPageFixtures.productComponent(3L, OrderType.NONE, 5);

            // when
            ProductComponentGroup group =
                    ProductComponentGroup.from(List.of(textComp1, textComp2, productComp));

            // then
            assertThat(group.nonTabComponents()).hasSize(1);
            assertThat(group.nonTabComponentIds()).containsExactly(3L);
        }

        @Test
        @DisplayName("여러 PRODUCT 컴포넌트가 있으면 모두 nonTabComponents에 포함된다")
        void multipleProductComponentsAllGoIntoNonTab() {
            // given
            DisplayComponent comp1 = ContentPageFixtures.productComponent(1L, OrderType.NONE, 10);
            DisplayComponent comp2 =
                    ContentPageFixtures.productComponent(2L, OrderType.LOW_PRICE, 20);
            DisplayComponent comp3 =
                    ContentPageFixtures.productComponent(3L, OrderType.HIGH_PRICE, 5);

            // when
            ProductComponentGroup group = ProductComponentGroup.from(List.of(comp1, comp2, comp3));

            // then
            assertThat(group.nonTabComponents()).hasSize(3);
            assertThat(group.nonTabComponentIds()).containsExactlyInAnyOrder(1L, 2L, 3L);
        }
    }
}
