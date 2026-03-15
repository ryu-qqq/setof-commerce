package com.ryuqq.setof.adapter.out.persistence.composite.content.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.composite.content.dto.FixedProductThumbnailDto;
import com.ryuqq.setof.adapter.out.persistence.composite.content.mapper.ContentCompositeMapper;
import com.ryuqq.setof.adapter.out.persistence.composite.content.repository.ContentCompositeQueryDslRepository;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.time.Instant;
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

/**
 * ComponentFixedProductQueryAdapterTest - FIXED 상품 조회 Adapter 단위 테스트.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>크로스 도메인 JOIN으로 고정 배치 상품 조회 및 tabId 기반 분류 검증.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ComponentFixedProductQueryAdapter 단위 테스트")
class ComponentFixedProductQueryAdapterTest {

    @Mock private ContentCompositeQueryDslRepository compositeRepository;

    @Mock private ContentCompositeMapper mapper;

    @InjectMocks private ComponentFixedProductQueryAdapter queryAdapter;

    // ========================================================================
    // 1. fetchFixedProducts 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchFixedProducts 메서드 테스트")
    class FetchFixedProductsTest {

        @Test
        @DisplayName("componentIds로 FIXED 상품 목록을 componentId 기준으로 그룹핑하여 반환합니다")
        void fetchFixedProducts_WithComponentIds_ReturnsGroupedByComponentId() {
            // given
            List<Long> componentIds = List.of(10L, 20L);
            FixedProductThumbnailDto dto1 = fixedDto(10L, null, 100L);
            FixedProductThumbnailDto dto2 = fixedDto(10L, null, 101L);
            FixedProductThumbnailDto dto3 = fixedDto(20L, null, 200L);
            ProductThumbnailSnapshot snapshot1 = ContentPageFixtures.snapshot(100L);
            ProductThumbnailSnapshot snapshot2 = ContentPageFixtures.snapshot(101L);
            ProductThumbnailSnapshot snapshot3 = ContentPageFixtures.snapshot(200L);

            given(compositeRepository.fetchFixedProductThumbnails(componentIds))
                    .willReturn(List.of(dto1, dto2, dto3));
            given(mapper.toSnapshot(dto1)).willReturn(snapshot1);
            given(mapper.toSnapshot(dto2)).willReturn(snapshot2);
            given(mapper.toSnapshot(dto3)).willReturn(snapshot3);

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    queryAdapter.fetchFixedProducts(componentIds);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(10L)).containsExactly(snapshot1, snapshot2);
            assertThat(result.get(20L)).containsExactly(snapshot3);
        }

        @Test
        @DisplayName("tabId가 있는 상품은 fetchFixedProducts에서 제외됩니다")
        void fetchFixedProducts_WithTabProducts_ExcludesTabProducts() {
            // given
            List<Long> componentIds = List.of(10L);
            FixedProductThumbnailDto noTabDto = fixedDto(10L, null, 100L);
            FixedProductThumbnailDto tabDto = fixedDto(10L, 50L, 101L);
            ProductThumbnailSnapshot snapshot = ContentPageFixtures.snapshot(100L);

            given(compositeRepository.fetchFixedProductThumbnails(componentIds))
                    .willReturn(List.of(noTabDto, tabDto));
            given(mapper.toSnapshot(noTabDto)).willReturn(snapshot);

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    queryAdapter.fetchFixedProducts(componentIds);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(10L)).hasSize(1);
            assertThat(result.get(10L)).containsExactly(snapshot);
        }

        @Test
        @DisplayName("componentIds가 null이면 빈 Map을 반환합니다")
        void fetchFixedProducts_WithNullComponentIds_ReturnsEmptyMap() {
            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    queryAdapter.fetchFixedProducts(null);

            // then
            assertThat(result).isEmpty();
            then(compositeRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("componentIds가 빈 리스트이면 빈 Map을 반환합니다")
        void fetchFixedProducts_WithEmptyComponentIds_ReturnsEmptyMap() {
            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    queryAdapter.fetchFixedProducts(List.of());

            // then
            assertThat(result).isEmpty();
            then(compositeRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("조회 결과가 없으면 빈 Map을 반환합니다")
        void fetchFixedProducts_WithNoResults_ReturnsEmptyMap() {
            // given
            List<Long> componentIds = List.of(10L);
            given(compositeRepository.fetchFixedProductThumbnails(componentIds))
                    .willReturn(List.of());

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    queryAdapter.fetchFixedProducts(componentIds);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 2. fetchFixedProductsByTab 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchFixedProductsByTab 메서드 테스트")
    class FetchFixedProductsByTabTest {

        @Test
        @DisplayName("tabId 기준으로 그룹핑된 상품 목록을 반환합니다")
        void fetchFixedProductsByTab_WithTabProducts_ReturnsGroupedByTabId() {
            // given
            List<Long> componentIds = List.of(10L);
            FixedProductThumbnailDto tab1Dto1 = fixedDto(10L, 50L, 100L);
            FixedProductThumbnailDto tab1Dto2 = fixedDto(10L, 50L, 101L);
            FixedProductThumbnailDto tab2Dto = fixedDto(10L, 60L, 200L);
            ProductThumbnailSnapshot snapshot1 = ContentPageFixtures.snapshot(100L);
            ProductThumbnailSnapshot snapshot2 = ContentPageFixtures.snapshot(101L);
            ProductThumbnailSnapshot snapshot3 = ContentPageFixtures.snapshot(200L);

            given(compositeRepository.fetchFixedProductThumbnails(componentIds))
                    .willReturn(List.of(tab1Dto1, tab1Dto2, tab2Dto));
            given(mapper.toSnapshot(tab1Dto1)).willReturn(snapshot1);
            given(mapper.toSnapshot(tab1Dto2)).willReturn(snapshot2);
            given(mapper.toSnapshot(tab2Dto)).willReturn(snapshot3);

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    queryAdapter.fetchFixedProductsByTab(componentIds);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(50L)).containsExactly(snapshot1, snapshot2);
            assertThat(result.get(60L)).containsExactly(snapshot3);
        }

        @Test
        @DisplayName("tabId가 없는 상품은 fetchFixedProductsByTab에서 제외됩니다")
        void fetchFixedProductsByTab_WithNonTabProducts_ExcludesNonTabProducts() {
            // given
            List<Long> componentIds = List.of(10L);
            FixedProductThumbnailDto noTabDto = fixedDto(10L, null, 100L);
            FixedProductThumbnailDto tabDto = fixedDto(10L, 50L, 101L);
            ProductThumbnailSnapshot tabSnapshot = ContentPageFixtures.snapshot(101L);

            given(compositeRepository.fetchFixedProductThumbnails(componentIds))
                    .willReturn(List.of(noTabDto, tabDto));
            given(mapper.toSnapshot(tabDto)).willReturn(tabSnapshot);

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    queryAdapter.fetchFixedProductsByTab(componentIds);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(50L)).containsExactly(tabSnapshot);
        }

        @Test
        @DisplayName("componentIds가 null이면 빈 Map을 반환합니다")
        void fetchFixedProductsByTab_WithNullComponentIds_ReturnsEmptyMap() {
            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    queryAdapter.fetchFixedProductsByTab(null);

            // then
            assertThat(result).isEmpty();
            then(compositeRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("componentIds가 빈 리스트이면 빈 Map을 반환합니다")
        void fetchFixedProductsByTab_WithEmptyComponentIds_ReturnsEmptyMap() {
            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    queryAdapter.fetchFixedProductsByTab(List.of());

            // then
            assertThat(result).isEmpty();
            then(compositeRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("tabId=0인 상품은 fetchFixedProductsByTab에서 제외됩니다")
        void fetchFixedProductsByTab_WithZeroTabId_ExcludesZeroTabProducts() {
            // given
            List<Long> componentIds = List.of(10L);
            FixedProductThumbnailDto zeroTabDto = fixedDto(10L, 0L, 100L);

            given(compositeRepository.fetchFixedProductThumbnails(componentIds))
                    .willReturn(List.of(zeroTabDto));

            // when
            Map<Long, List<ProductThumbnailSnapshot>> result =
                    queryAdapter.fetchFixedProductsByTab(componentIds);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 헬퍼 메서드
    // ========================================================================

    private FixedProductThumbnailDto fixedDto(long componentId, Long tabId, long productGroupId) {
        return new FixedProductThumbnailDto(
                componentId,
                tabId,
                productGroupId,
                "전시명",
                "https://display.img/a.jpg",
                1L,
                "상품명-" + productGroupId,
                5L,
                "브랜드A",
                10000,
                8000,
                0,
                Instant.parse("2025-01-01T00:00:00Z"),
                "https://thumb.img/" + productGroupId + ".jpg");
    }
}
