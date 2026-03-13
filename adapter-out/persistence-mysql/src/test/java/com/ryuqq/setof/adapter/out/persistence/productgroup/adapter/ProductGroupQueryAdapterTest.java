package com.ryuqq.setof.adapter.out.persistence.productgroup.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.productgroup.ProductGroupJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionValueJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.mapper.ProductGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupQueryDslRepository;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * ProductGroupQueryAdapterTest - 상품그룹 Query Adapter 단위 테스트.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductGroupQueryAdapter 단위 테스트")
class ProductGroupQueryAdapterTest {

    @Mock private ProductGroupQueryDslRepository queryDslRepository;
    @Mock private ProductGroupJpaEntityMapper mapper;

    @InjectMocks private ProductGroupQueryAdapter queryAdapter;

    // ========================================================================
    // 1. findById 테스트
    // ========================================================================

    @Nested
    @DisplayName("findById 메서드 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회 시 Domain을 반환합니다")
        void findById_WithExistingId_ReturnsDomain() {
            // given
            ProductGroupId id = ProductGroupId.of(1L);
            ProductGroupJpaEntity entity = ProductGroupJpaEntityFixtures.activeEntity();
            List<SellerOptionGroupJpaEntity> groups =
                    ProductGroupJpaEntityFixtures.defaultOptionGroupEntities();
            List<Long> groupIds = groups.stream().map(SellerOptionGroupJpaEntity::getId).toList();
            List<SellerOptionValueJpaEntity> values =
                    ProductGroupJpaEntityFixtures.defaultOptionValueEntities();
            ProductGroup domain = ProductGroupFixtures.activeProductGroup(1L);

            given(queryDslRepository.findById(1L)).willReturn(Optional.of(entity));
            given(queryDslRepository.findOptionGroupsByProductGroupId(entity.getId()))
                    .willReturn(groups);
            given(queryDslRepository.findOptionValuesByOptionGroupIds(groupIds)).willReturn(values);
            given(mapper.toDomain(entity, List.of(), groups, values)).willReturn(domain);

            // when
            Optional<ProductGroup> result = queryAdapter.findById(id);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void findById_WithNonExistingId_ReturnsEmpty() {
            // given
            ProductGroupId id = ProductGroupId.of(999L);
            given(queryDslRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Optional<ProductGroup> result = queryAdapter.findById(id);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 2. findByIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByIds 메서드 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 상품그룹 목록을 반환합니다")
        void findByIds_WithValidIds_ReturnsDomainList() {
            // given
            List<ProductGroupId> ids = List.of(ProductGroupId.of(1L), ProductGroupId.of(2L));
            ProductGroupJpaEntity entity1 = ProductGroupJpaEntityFixtures.activeEntity(1L);
            ProductGroupJpaEntity entity2 = ProductGroupJpaEntityFixtures.activeEntity(2L);
            ProductGroup domain1 = ProductGroupFixtures.activeProductGroup(1L);
            ProductGroup domain2 = ProductGroupFixtures.activeProductGroup(2L);

            given(queryDslRepository.findByIds(List.of(1L, 2L)))
                    .willReturn(List.of(entity1, entity2));
            given(queryDslRepository.findOptionGroupsByProductGroupIds(List.of(1L, 2L)))
                    .willReturn(List.of());
            given(queryDslRepository.findOptionValuesByOptionGroupIds(List.of()))
                    .willReturn(List.of());
            given(mapper.toDomain(entity1, List.of(), List.of(), List.of())).willReturn(domain1);
            given(mapper.toDomain(entity2, List.of(), List.of(), List.of())).willReturn(domain2);

            // when
            List<ProductGroup> result = queryAdapter.findByIds(ids);

            // then
            assertThat(result).hasSize(2);
            then(queryDslRepository).should().findByIds(List.of(1L, 2L));
        }

        @Test
        @DisplayName("결과가 없을 때 빈 리스트를 반환합니다")
        void findByIds_WithNoResults_ReturnsEmptyList() {
            // given
            List<ProductGroupId> ids = List.of(ProductGroupId.of(999L));
            given(queryDslRepository.findByIds(List.of(999L))).willReturn(List.of());

            // when
            List<ProductGroup> result = queryAdapter.findByIds(ids);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회 시 빈 리스트를 반환합니다")
        void findByIds_WithEmptyList_ReturnsEmptyList() {
            // given
            given(queryDslRepository.findByIds(List.of())).willReturn(List.of());

            // when
            List<ProductGroup> result = queryAdapter.findByIds(List.of());

            // then
            assertThat(result).isEmpty();
        }
    }
}
