package com.ryuqq.setof.application.contentpage.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.contentpage.ContentPageProductFixtures;
import com.ryuqq.setof.application.contentpage.port.out.ComponentFixedProductQueryPort;
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
@DisplayName("FixedProductReadManager 단위 테스트")
class FixedProductReadManagerTest {

    @InjectMocks private FixedProductReadManager sut;

    @Mock private ComponentFixedProductQueryPort queryPort;

    @Nested
    @DisplayName("fetchFixedProducts() - 컴포넌트별 FIXED 상품 조회")
    class FetchFixedProductsTest {

        @Test
        @DisplayName("컴포넌트 ID 목록으로 FIXED 상품 맵을 조회하여 반환한다")
        void fetchFixedProducts_ValidComponentIds_ReturnsProductMap() {
            // given
            List<Long> componentIds = List.of(1L, 2L);
            Map<Long, List<ProductThumbnailSnapshot>> expected =
                    Map.of(
                            1L, ContentPageProductFixtures.productSnapshots(101L, 102L),
                            2L, ContentPageProductFixtures.productSnapshots(201L));

            given(queryPort.fetchFixedProducts(componentIds)).willReturn(expected);

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result = sut.fetchFixedProducts(componentIds);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchFixedProducts(componentIds);
        }

        @Test
        @DisplayName("빈 컴포넌트 ID 목록을 전달하면 빈 맵을 반환한다")
        void fetchFixedProducts_EmptyComponentIds_ReturnsEmptyMap() {
            // given
            List<Long> componentIds = List.of();

            given(queryPort.fetchFixedProducts(componentIds)).willReturn(Map.of());

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result = sut.fetchFixedProducts(componentIds);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().fetchFixedProducts(componentIds);
        }

        @Test
        @DisplayName("조회 결과가 없는 컴포넌트는 맵에 포함되지 않는다")
        void fetchFixedProducts_NoMatchingProducts_ReturnsEmptyMap() {
            // given
            List<Long> componentIds = List.of(999L);

            given(queryPort.fetchFixedProducts(componentIds)).willReturn(Map.of());

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result = sut.fetchFixedProducts(componentIds);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().fetchFixedProducts(componentIds);
        }
    }

    @Nested
    @DisplayName("fetchFixedProductsByTab() - 탭별 FIXED 상품 조회")
    class FetchFixedProductsByTabTest {

        @Test
        @DisplayName("탭 컴포넌트 ID 목록으로 탭별 FIXED 상품 맵을 조회하여 반환한다")
        void fetchFixedProductsByTab_ValidComponentIds_ReturnsTabProductMap() {
            // given
            List<Long> componentIds = List.of(10L, 20L);
            Map<Long, List<ProductThumbnailSnapshot>> expected =
                    Map.of(
                            100L, ContentPageProductFixtures.productSnapshots(301L, 302L),
                            200L, ContentPageProductFixtures.productSnapshots(401L));

            given(queryPort.fetchFixedProductsByTab(componentIds)).willReturn(expected);

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    sut.fetchFixedProductsByTab(componentIds);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchFixedProductsByTab(componentIds);
        }

        @Test
        @DisplayName("빈 컴포넌트 ID 목록을 전달하면 빈 맵을 반환한다")
        void fetchFixedProductsByTab_EmptyComponentIds_ReturnsEmptyMap() {
            // given
            List<Long> componentIds = List.of();

            given(queryPort.fetchFixedProductsByTab(componentIds)).willReturn(Map.of());

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    sut.fetchFixedProductsByTab(componentIds);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().fetchFixedProductsByTab(componentIds);
        }

        @Test
        @DisplayName("조회 결과가 없으면 빈 맵을 반환한다")
        void fetchFixedProductsByTab_NoMatchingProducts_ReturnsEmptyMap() {
            // given
            List<Long> componentIds = List.of(999L);

            given(queryPort.fetchFixedProductsByTab(componentIds)).willReturn(Map.of());

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    sut.fetchFixedProductsByTab(componentIds);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().fetchFixedProductsByTab(componentIds);
        }
    }
}
