package com.ryuqq.setof.adapter.out.persistence.composite.content.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.composite.content.dto.AutoProductThumbnailDto;
import com.ryuqq.setof.adapter.out.persistence.composite.content.mapper.ContentCompositeMapper;
import com.ryuqq.setof.adapter.out.persistence.composite.content.repository.ContentCompositeQueryDslRepository;
import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * ComponentAutoProductQueryAdapterTest - AUTO 상품 조회 Adapter 단위 테스트.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>크로스 도메인 JOIN으로 카테고리/브랜드 기반 동적 상품 조회 검증.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ComponentAutoProductQueryAdapter 단위 테스트")
class ComponentAutoProductQueryAdapterTest {

    @Mock private ContentCompositeQueryDslRepository compositeRepository;

    @Mock private ContentCompositeMapper mapper;

    @InjectMocks private ComponentAutoProductQueryAdapter queryAdapter;

    // ========================================================================
    // 1. fetchAutoProducts 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchAutoProducts 메서드 테스트")
    class FetchAutoProductsTest {

        @Test
        @DisplayName("AutoProductCriteria로 상품 목록을 반환합니다")
        void fetchAutoProducts_WithValidCriteria_ReturnsSnapshotList() {
            // given
            List<Long> categoryIds = List.of(1L, 2L);
            List<Long> brandIds = List.of(10L);
            int limit = 10;
            AutoProductCriteria criteria =
                    new AutoProductCriteria(1L, 0L, categoryIds, brandIds, limit);

            AutoProductThumbnailDto dto1 = autoDto(100L);
            AutoProductThumbnailDto dto2 = autoDto(101L);
            ProductThumbnailSnapshot snapshot1 = ContentPageFixtures.snapshot(100L);
            ProductThumbnailSnapshot snapshot2 = ContentPageFixtures.snapshot(101L);

            given(compositeRepository.fetchAutoProductThumbnails(categoryIds, brandIds, limit))
                    .willReturn(List.of(dto1, dto2));
            given(mapper.toSnapshot(dto1)).willReturn(snapshot1);
            given(mapper.toSnapshot(dto2)).willReturn(snapshot2);

            // when
            List<ProductThumbnailSnapshot> result = queryAdapter.fetchAutoProducts(criteria);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(snapshot1, snapshot2);
            then(compositeRepository)
                    .should()
                    .fetchAutoProductThumbnails(categoryIds, brandIds, limit);
        }

        @Test
        @DisplayName("조건에 맞는 상품이 없으면 빈 리스트를 반환합니다")
        void fetchAutoProducts_WithNoResults_ReturnsEmptyList() {
            // given
            AutoProductCriteria criteria =
                    new AutoProductCriteria(1L, 0L, List.of(), List.of(), 10);

            given(compositeRepository.fetchAutoProductThumbnails(List.of(), List.of(), 10))
                    .willReturn(List.of());

            // when
            List<ProductThumbnailSnapshot> result = queryAdapter.fetchAutoProducts(criteria);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("categoryIds가 비어있어도 brandIds로 조회합니다")
        void fetchAutoProducts_WithOnlyBrandIds_ReturnsSnapshotList() {
            // given
            List<Long> categoryIds = List.of();
            List<Long> brandIds = List.of(5L, 6L);
            int limit = 5;
            AutoProductCriteria criteria =
                    new AutoProductCriteria(1L, 0L, categoryIds, brandIds, limit);

            AutoProductThumbnailDto dto = autoDto(200L);
            ProductThumbnailSnapshot snapshot = ContentPageFixtures.snapshot(200L);

            given(compositeRepository.fetchAutoProductThumbnails(categoryIds, brandIds, limit))
                    .willReturn(List.of(dto));
            given(mapper.toSnapshot(dto)).willReturn(snapshot);

            // when
            List<ProductThumbnailSnapshot> result = queryAdapter.fetchAutoProducts(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(snapshot);
        }
    }

    // ========================================================================
    // 헬퍼 메서드
    // ========================================================================

    private AutoProductThumbnailDto autoDto(long productGroupId) {
        return new AutoProductThumbnailDto(
                productGroupId,
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
