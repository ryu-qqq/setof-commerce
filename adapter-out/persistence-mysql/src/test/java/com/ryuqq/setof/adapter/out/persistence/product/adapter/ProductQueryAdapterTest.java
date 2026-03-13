package com.ryuqq.setof.adapter.out.persistence.product.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.product.ProductJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductOptionMappingJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.mapper.ProductJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductQueryDslRepository;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.id.ProductId;
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
 * ProductQueryAdapterTest - 상품 Query Adapter 단위 테스트.
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
@DisplayName("ProductQueryAdapter 단위 테스트")
class ProductQueryAdapterTest {

    @Mock private ProductQueryDslRepository queryDslRepository;
    @Mock private ProductJpaEntityMapper mapper;

    @InjectMocks private ProductQueryAdapter queryAdapter;

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
            ProductId productId = ProductId.of(1L);
            ProductJpaEntity entity = ProductJpaEntityFixtures.activeEntity();
            List<ProductOptionMappingJpaEntity> mappings =
                    ProductJpaEntityFixtures.defaultMappingEntities();
            Product domain = com.setof.commerce.domain.product.ProductFixtures.activeProduct();

            given(queryDslRepository.findById(1L)).willReturn(Optional.of(entity));
            given(queryDslRepository.findOptionMappingsByProductId(entity.getId()))
                    .willReturn(mappings);
            given(mapper.toDomain(entity, mappings)).willReturn(domain);

            // when
            Optional<Product> result = queryAdapter.findById(productId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void findById_WithNonExistingId_ReturnsEmpty() {
            // given
            ProductId productId = ProductId.of(999L);
            given(queryDslRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Optional<Product> result = queryAdapter.findById(productId);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 2. findByProductGroupId 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByProductGroupId 메서드 테스트")
    class FindByProductGroupIdTest {

        @Test
        @DisplayName("productGroupId로 상품 목록을 반환합니다")
        void findByProductGroupId_WithValidId_ReturnsDomainList() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            ProductJpaEntity entity1 = ProductJpaEntityFixtures.activeEntity(1L);
            ProductJpaEntity entity2 = ProductJpaEntityFixtures.activeEntity(2L);
            Product domain1 = com.setof.commerce.domain.product.ProductFixtures.activeProduct(1L);
            Product domain2 = com.setof.commerce.domain.product.ProductFixtures.activeProduct(2L);

            given(queryDslRepository.findByProductGroupId(1L))
                    .willReturn(List.of(entity1, entity2));
            given(queryDslRepository.findOptionMappingsByProductIds(List.of(1L, 2L)))
                    .willReturn(List.of());
            given(mapper.toDomain(entity1, List.of())).willReturn(domain1);
            given(mapper.toDomain(entity2, List.of())).willReturn(domain2);

            // when
            List<Product> result = queryAdapter.findByProductGroupId(productGroupId);

            // then
            assertThat(result).hasSize(2);
            then(queryDslRepository).should().findByProductGroupId(1L);
        }

        @Test
        @DisplayName("결과가 없을 때 빈 리스트를 반환합니다")
        void findByProductGroupId_WithNoResults_ReturnsEmptyList() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(999L);
            given(queryDslRepository.findByProductGroupId(999L)).willReturn(List.of());

            // when
            List<Product> result = queryAdapter.findByProductGroupId(productGroupId);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 3. findByIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByIds 메서드 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 상품 목록을 반환합니다")
        void findByIds_WithValidIds_ReturnsDomainList() {
            // given
            List<ProductId> productIds = List.of(ProductId.of(1L), ProductId.of(2L));
            ProductJpaEntity entity1 = ProductJpaEntityFixtures.activeEntity(1L);
            ProductJpaEntity entity2 = ProductJpaEntityFixtures.activeEntity(2L);
            Product domain1 = com.setof.commerce.domain.product.ProductFixtures.activeProduct(1L);
            Product domain2 = com.setof.commerce.domain.product.ProductFixtures.activeProduct(2L);

            given(queryDslRepository.findByIdIn(List.of(1L, 2L)))
                    .willReturn(List.of(entity1, entity2));
            given(queryDslRepository.findOptionMappingsByProductIds(List.of(1L, 2L)))
                    .willReturn(List.of());
            given(mapper.toDomain(entity1, List.of())).willReturn(domain1);
            given(mapper.toDomain(entity2, List.of())).willReturn(domain2);

            // when
            List<Product> result = queryAdapter.findByIds(productIds);

            // then
            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회 시 빈 리스트를 반환합니다")
        void findByIds_WithEmptyList_ReturnsEmptyList() {
            // given
            given(queryDslRepository.findByIdIn(List.of())).willReturn(List.of());

            // when
            List<Product> result = queryAdapter.findByIds(List.of());

            // then
            assertThat(result).isEmpty();
        }
    }
}
