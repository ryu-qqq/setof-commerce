package com.ryuqq.setof.application.contentpage.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.ryuqq.setof.application.contentpage.ContentPageProductFixtures;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ComponentProductBundle;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ComponentProductReadFacade 단위 테스트")
class ComponentProductReadFacadeTest {

    @InjectMocks private ComponentProductReadFacade sut;

    @Mock private FixedProductReadManager fixedProductReadManager;
    @Mock private AutoProductReadManager autoProductReadManager;

    @Nested
    @DisplayName("fetchComponentProducts() - 컴포넌트 상품 조회")
    class FetchComponentProductsTest {

        @Test
        @DisplayName("빈 컴포넌트 목록을 전달하면 empty ComponentProductBundle을 반환한다")
        void fetchComponentProducts_EmptyComponents_ReturnsEmptyBundle() {
            // given
            List<DisplayComponent> components = List.of();

            // when
            ComponentProductBundle result = sut.fetchComponentProducts(components);

            // then
            assertThat(result.componentProducts()).isEmpty();
            assertThat(result.tabProducts()).isEmpty();
            then(fixedProductReadManager).shouldHaveNoInteractions();
            then(autoProductReadManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("상품 컴포넌트가 없는 비상품 컴포넌트만 있으면 empty ComponentProductBundle을 반환한다")
        void fetchComponentProducts_OnlyNonProductComponents_ReturnsEmptyBundle() {
            // given - TEXT 타입은 상품 컴포넌트가 아님
            List<DisplayComponent> components =
                    List.of(
                            ContentPageProductFixtures.textComponent(1L),
                            ContentPageProductFixtures.textComponent(2L));

            // when
            ComponentProductBundle result = sut.fetchComponentProducts(components);

            // then
            assertThat(result.componentProducts()).isEmpty();
            assertThat(result.tabProducts()).isEmpty();
            then(fixedProductReadManager).shouldHaveNoInteractions();
            then(autoProductReadManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Non-TAB 상품 컴포넌트만 있으면 componentProducts에 결과가 담긴다")
        void fetchComponentProducts_OnlyNonTabComponents_ReturnsComponentProducts() {
            // given
            long componentId = 1L;
            DisplayComponent productComponent =
                    ContentPageProductFixtures.productComponent(componentId);
            List<DisplayComponent> components = List.of(productComponent);

            List<Long> expectedNonTabIds = List.of(componentId);
            Map<Long, List<ProductThumbnailSnapshot>> fixedResult =
                    Map.of(componentId, ContentPageProductFixtures.productSnapshots(101L, 102L));

            given(fixedProductReadManager.fetchFixedProducts(expectedNonTabIds))
                    .willReturn(fixedResult);
            given(autoProductReadManager.fetchAutoProducts(any(AutoProductCriteria.class)))
                    .willReturn(ContentPageProductFixtures.productSnapshots(201L));

            // when
            ComponentProductBundle result = sut.fetchComponentProducts(components);

            // then
            assertThat(result.componentProducts()).containsKey(componentId);
            assertThat(result.tabProducts()).isEmpty();
            then(fixedProductReadManager).should().fetchFixedProducts(expectedNonTabIds);
            then(fixedProductReadManager).should(never()).fetchFixedProductsByTab(anyList());
        }

        @Test
        @DisplayName("TAB 상품 컴포넌트만 있으면 tabProducts에 결과가 담긴다")
        void fetchComponentProducts_OnlyTabComponents_ReturnsTabProducts() {
            // given
            long componentId = 10L;
            long tabId = 100L;
            DisplayComponent tabComponent =
                    ContentPageProductFixtures.tabComponent(componentId, tabId);
            List<DisplayComponent> components = List.of(tabComponent);

            List<Long> expectedTabIds = List.of(componentId);
            Map<Long, List<ProductThumbnailSnapshot>> fixedByTabResult =
                    Map.of(tabId, ContentPageProductFixtures.productSnapshots(301L, 302L));

            given(fixedProductReadManager.fetchFixedProductsByTab(expectedTabIds))
                    .willReturn(fixedByTabResult);
            given(autoProductReadManager.fetchAutoProducts(any(AutoProductCriteria.class)))
                    .willReturn(ContentPageProductFixtures.productSnapshots(401L));

            // when
            ComponentProductBundle result = sut.fetchComponentProducts(components);

            // then
            assertThat(result.componentProducts()).isEmpty();
            assertThat(result.tabProducts()).containsKey(tabId);
            then(fixedProductReadManager).should().fetchFixedProductsByTab(expectedTabIds);
            then(fixedProductReadManager).should(never()).fetchFixedProducts(anyList());
        }

        @Test
        @DisplayName("Non-TAB과 TAB 컴포넌트가 모두 있으면 두 결과가 모두 담긴다")
        void fetchComponentProducts_MixedComponents_ReturnsBothResults() {
            // given
            long productComponentId = 1L;
            long tabComponentId = 10L;
            long tabId = 100L;

            DisplayComponent productComponent =
                    ContentPageProductFixtures.productComponent(productComponentId);
            DisplayComponent tabComponent =
                    ContentPageProductFixtures.tabComponent(tabComponentId, tabId);
            List<DisplayComponent> components = List.of(productComponent, tabComponent);

            Map<Long, List<ProductThumbnailSnapshot>> fixedByComponent =
                    Map.of(productComponentId, ContentPageProductFixtures.productSnapshots(101L));
            Map<Long, List<ProductThumbnailSnapshot>> fixedByTab =
                    Map.of(tabId, ContentPageProductFixtures.productSnapshots(301L));

            given(fixedProductReadManager.fetchFixedProducts(List.of(productComponentId)))
                    .willReturn(fixedByComponent);
            given(fixedProductReadManager.fetchFixedProductsByTab(List.of(tabComponentId)))
                    .willReturn(fixedByTab);
            given(autoProductReadManager.fetchAutoProducts(any(AutoProductCriteria.class)))
                    .willReturn(List.of());

            // when
            ComponentProductBundle result = sut.fetchComponentProducts(components);

            // then
            assertThat(result.componentProducts()).containsKey(productComponentId);
            assertThat(result.tabProducts()).containsKey(tabId);
            then(fixedProductReadManager).should().fetchFixedProducts(List.of(productComponentId));
            then(fixedProductReadManager).should().fetchFixedProductsByTab(List.of(tabComponentId));
        }

        @Test
        @DisplayName("Non-TAB 컴포넌트가 없으면 fetchFixedProducts가 호출되지 않는다")
        void fetchComponentProducts_TabOnly_FixedProductsNotCalled() {
            // given
            long tabComponentId = 10L;
            long tabId = 100L;
            DisplayComponent tabComponent =
                    ContentPageProductFixtures.tabComponent(tabComponentId, tabId);
            List<DisplayComponent> components = List.of(tabComponent);

            given(fixedProductReadManager.fetchFixedProductsByTab(List.of(tabComponentId)))
                    .willReturn(Map.of());
            given(autoProductReadManager.fetchAutoProducts(any(AutoProductCriteria.class)))
                    .willReturn(List.of());

            // when
            sut.fetchComponentProducts(components);

            // then
            then(fixedProductReadManager).should(never()).fetchFixedProducts(anyList());
        }

        @Test
        @DisplayName("TAB 컴포넌트가 없으면 fetchFixedProductsByTab이 호출되지 않는다")
        void fetchComponentProducts_NonTabOnly_FixedProductsByTabNotCalled() {
            // given
            long componentId = 1L;
            DisplayComponent productComponent =
                    ContentPageProductFixtures.productComponent(componentId);
            List<DisplayComponent> components = List.of(productComponent);

            given(fixedProductReadManager.fetchFixedProducts(List.of(componentId)))
                    .willReturn(Map.of());
            given(autoProductReadManager.fetchAutoProducts(any(AutoProductCriteria.class)))
                    .willReturn(List.of());

            // when
            sut.fetchComponentProducts(components);

            // then
            then(fixedProductReadManager).should(never()).fetchFixedProductsByTab(anyList());
        }
    }
}
