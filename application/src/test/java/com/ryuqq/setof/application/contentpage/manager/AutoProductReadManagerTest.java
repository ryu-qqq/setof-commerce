package com.ryuqq.setof.application.contentpage.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.contentpage.ContentPageProductFixtures;
import com.ryuqq.setof.application.contentpage.port.out.ComponentAutoProductQueryPort;
import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.List;
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
@DisplayName("AutoProductReadManager 단위 테스트")
class AutoProductReadManagerTest {

    @InjectMocks private AutoProductReadManager sut;

    @Mock private ComponentAutoProductQueryPort queryPort;

    @Nested
    @DisplayName("fetchAutoProducts() - AUTO 상품 조회")
    class FetchAutoProductsTest {

        @Test
        @DisplayName("컴포넌트 레벨 조건으로 AUTO 상품 목록을 조회하여 반환한다")
        void fetchAutoProducts_ComponentLevelCriteria_ReturnsProductList() {
            // given
            AutoProductCriteria criteria =
                    ContentPageProductFixtures.autoProductCriteriaForComponent(1L);
            List<ProductThumbnailSnapshot> expected =
                    ContentPageProductFixtures.productSnapshots(201L, 202L, 203L);

            given(queryPort.fetchAutoProducts(criteria)).willReturn(expected);

            // when
            List<ProductThumbnailSnapshot> result = sut.fetchAutoProducts(criteria);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchAutoProducts(criteria);
        }

        @Test
        @DisplayName("탭 레벨 조건으로 AUTO 상품 목록을 조회하여 반환한다")
        void fetchAutoProducts_TabLevelCriteria_ReturnsProductList() {
            // given
            AutoProductCriteria criteria =
                    ContentPageProductFixtures.autoProductCriteriaForTab(10L, 100L);
            List<ProductThumbnailSnapshot> expected =
                    ContentPageProductFixtures.productSnapshots(301L, 302L);

            given(queryPort.fetchAutoProducts(criteria)).willReturn(expected);

            // when
            List<ProductThumbnailSnapshot> result = sut.fetchAutoProducts(criteria);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchAutoProducts(criteria);
        }

        @Test
        @DisplayName("조건에 매칭되는 상품이 없으면 빈 목록을 반환한다")
        void fetchAutoProducts_NoMatchingProducts_ReturnsEmptyList() {
            // given
            AutoProductCriteria criteria =
                    ContentPageProductFixtures.autoProductCriteriaForComponent(999L);

            given(queryPort.fetchAutoProducts(criteria)).willReturn(List.of());

            // when
            List<ProductThumbnailSnapshot> result = sut.fetchAutoProducts(criteria);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().fetchAutoProducts(criteria);
        }

        @Test
        @DisplayName("카테고리 필터가 있는 조건으로 AUTO 상품을 조회한다")
        void fetchAutoProducts_WithCategoryFilter_ReturnsFilteredProducts() {
            // given
            AutoProductCriteria criteria = AutoProductCriteria.ofComponent(1L, 500L, List.of(), 10);
            List<ProductThumbnailSnapshot> expected =
                    ContentPageProductFixtures.productSnapshots(501L);

            given(queryPort.fetchAutoProducts(criteria)).willReturn(expected);

            // when
            List<ProductThumbnailSnapshot> result = sut.fetchAutoProducts(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.getFirst().productGroupId()).isEqualTo(501L);
            then(queryPort).should().fetchAutoProducts(criteria);
        }

        @Test
        @DisplayName("브랜드 필터가 있는 조건으로 AUTO 상품을 조회한다")
        void fetchAutoProducts_WithBrandFilter_ReturnsFilteredProducts() {
            // given
            AutoProductCriteria criteria =
                    AutoProductCriteria.ofComponent(1L, 0L, List.of(10L, 20L), 15);
            List<ProductThumbnailSnapshot> expected =
                    ContentPageProductFixtures.productSnapshots(601L, 602L);

            given(queryPort.fetchAutoProducts(criteria)).willReturn(expected);

            // when
            List<ProductThumbnailSnapshot> result = sut.fetchAutoProducts(criteria);

            // then
            assertThat(result).hasSize(2);
            then(queryPort).should().fetchAutoProducts(criteria);
        }
    }
}
