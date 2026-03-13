package com.ryuqq.setof.adapter.out.persistence.product.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.product.ProductJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductOptionMappingJpaEntity;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductOptionMapping;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductJpaEntityMapperTest - 상품 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ProductJpaEntityMapper 단위 테스트")
class ProductJpaEntityMapperTest {

    private ProductJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("ACTIVE 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveProduct_ConvertsCorrectly() {
            // given
            Product domain = com.setof.commerce.domain.product.ProductFixtures.activeProduct();

            // when
            ProductJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getProductGroupId()).isEqualTo(domain.productGroupIdValue());
            assertThat(entity.getRegularPrice()).isEqualTo(domain.regularPriceValue());
            assertThat(entity.getCurrentPrice()).isEqualTo(domain.currentPriceValue());
            assertThat(entity.getDiscountRate()).isEqualTo(domain.discountRate());
            assertThat(entity.getStockQuantity()).isEqualTo(domain.stockQuantity());
            assertThat(entity.getStatus()).isEqualTo(domain.status().name());
            assertThat(entity.getSortOrder()).isEqualTo(domain.sortOrder());
        }

        @Test
        @DisplayName("DELETED 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedProduct_ConvertsCorrectly() {
            // given
            Product domain = com.setof.commerce.domain.product.ProductFixtures.deletedProduct();

            // when
            ProductJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getStatus()).isEqualTo(ProductStatus.DELETED.name());
        }

        @Test
        @DisplayName("salePrice가 null인 Domain을 Entity로 변환합니다")
        void toEntity_WithNullSalePrice_ConvertsCorrectly() {
            // given
            Product domain =
                    com.ryuqq.setof.domain.product.aggregate.Product.reconstitute(
                            com.ryuqq.setof.domain.product.id.ProductId.of(99L),
                            com.ryuqq.setof.domain.productgroup.id.ProductGroupId.of(1L),
                            null,
                            com.ryuqq.setof.domain.common.vo.Money.of(10000),
                            com.ryuqq.setof.domain.common.vo.Money.of(10000),
                            null,
                            0,
                            100,
                            com.ryuqq.setof.domain.product.vo.ProductStatus.ACTIVE,
                            0,
                            java.util.List.of(),
                            java.time.Instant.now(),
                            java.time.Instant.now());

            // when
            ProductJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getSalePrice()).isNull();
        }
    }

    // ========================================================================
    // 2. toMappingEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toMappingEntity 메서드 테스트")
    class ToMappingEntityTest {

        @Test
        @DisplayName("활성 ProductOptionMapping을 Entity로 변환합니다")
        void toMappingEntity_WithActiveMapping_ConvertsCorrectly() {
            // given
            ProductOptionMapping mapping =
                    com.setof.commerce.domain.product.ProductFixtures.activeProductOptionMapping();

            // when
            ProductOptionMappingJpaEntity entity = mapper.toMappingEntity(mapping);

            // then
            assertThat(entity.getId()).isEqualTo(mapping.idValue());
            assertThat(entity.getProductId()).isEqualTo(mapping.productIdValue());
            assertThat(entity.getSellerOptionValueId())
                    .isEqualTo(mapping.sellerOptionValueIdValue());
            assertThat(entity.isDeleted()).isFalse();
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("삭제된 ProductOptionMapping을 Entity로 변환합니다")
        void toMappingEntity_WithDeletedMapping_ConvertsCorrectly() {
            // given
            ProductOptionMapping mapping =
                    com.setof.commerce.domain.product.ProductFixtures.deletedProductOptionMapping();

            // when
            ProductOptionMappingJpaEntity entity = mapper.toMappingEntity(mapping);

            // then
            assertThat(entity.isDeleted()).isTrue();
            assertThat(entity.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("명시적 productId를 지정하여 Entity로 변환합니다")
        void toMappingEntity_WithExplicitProductId_ConvertsCorrectly() {
            // given
            ProductOptionMapping mapping =
                    com.setof.commerce.domain.product.ProductFixtures.activeProductOptionMapping();
            Long explicitProductId = 999L;

            // when
            ProductOptionMappingJpaEntity entity =
                    mapper.toMappingEntity(mapping, explicitProductId);

            // then
            assertThat(entity.getProductId()).isEqualTo(explicitProductId);
            assertThat(entity.getSellerOptionValueId())
                    .isEqualTo(mapping.sellerOptionValueIdValue());
        }
    }

    // ========================================================================
    // 3. toDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("ACTIVE 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveEntity_ConvertsCorrectly() {
            // given
            ProductJpaEntity entity = ProductJpaEntityFixtures.activeEntity();
            List<ProductOptionMappingJpaEntity> mappings =
                    ProductJpaEntityFixtures.defaultMappingEntities();

            // when
            Product domain = mapper.toDomain(entity, mappings);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.productGroupIdValue()).isEqualTo(entity.getProductGroupId());
            assertThat(domain.regularPriceValue()).isEqualTo(entity.getRegularPrice());
            assertThat(domain.currentPriceValue()).isEqualTo(entity.getCurrentPrice());
            assertThat(domain.discountRate()).isEqualTo(entity.getDiscountRate());
            assertThat(domain.stockQuantity()).isEqualTo(entity.getStockQuantity());
            assertThat(domain.status().name()).isEqualTo(entity.getStatus());
            assertThat(domain.sortOrder()).isEqualTo(entity.getSortOrder());
        }

        @Test
        @DisplayName("salePrice가 null인 Entity를 Domain으로 변환합니다")
        void toDomain_WithNullSalePrice_ConvertsCorrectly() {
            // given
            ProductJpaEntity entity = ProductJpaEntityFixtures.entityWithoutSalePrice();
            List<ProductOptionMappingJpaEntity> mappings = List.of();

            // when
            Product domain = mapper.toDomain(entity, mappings);

            // then
            assertThat(domain.salePriceValue()).isNull();
        }

        @Test
        @DisplayName("옵션 매핑이 없는 Entity를 Domain으로 변환합니다")
        void toDomain_WithEmptyMappings_ConvertsCorrectly() {
            // given
            ProductJpaEntity entity = ProductJpaEntityFixtures.activeEntity();
            List<ProductOptionMappingJpaEntity> mappings =
                    ProductJpaEntityFixtures.emptyMappingEntities();

            // when
            Product domain = mapper.toDomain(entity, mappings);

            // then
            assertThat(domain.optionMappings()).isEmpty();
        }

        @Test
        @DisplayName("DELETED 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ConvertsCorrectly() {
            // given
            ProductJpaEntity entity = ProductJpaEntityFixtures.deletedEntity();
            List<ProductOptionMappingJpaEntity> mappings = List.of();

            // when
            Product domain = mapper.toDomain(entity, mappings);

            // then
            assertThat(domain.status()).isEqualTo(ProductStatus.DELETED);
        }
    }

    // ========================================================================
    // 4. 양방향 변환 테스트
    // ========================================================================

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            // given
            ProductJpaEntity original = ProductJpaEntityFixtures.activeEntity();
            List<ProductOptionMappingJpaEntity> mappings =
                    ProductJpaEntityFixtures.emptyMappingEntities();

            // when
            Product domain = mapper.toDomain(original, mappings);
            ProductJpaEntity converted = mapper.toEntity(domain);

            // then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getProductGroupId()).isEqualTo(original.getProductGroupId());
            assertThat(converted.getRegularPrice()).isEqualTo(original.getRegularPrice());
            assertThat(converted.getCurrentPrice()).isEqualTo(original.getCurrentPrice());
            assertThat(converted.getDiscountRate()).isEqualTo(original.getDiscountRate());
            assertThat(converted.getStockQuantity()).isEqualTo(original.getStockQuantity());
            assertThat(converted.getStatus()).isEqualTo(original.getStatus());
            assertThat(converted.getSortOrder()).isEqualTo(original.getSortOrder());
        }

        @Test
        @DisplayName("Domain -> Entity -> Domain 변환 시 데이터가 보존됩니다")
        void roundTrip_DomainToEntityToDomain_PreservesData() {
            // given
            Product original = com.setof.commerce.domain.product.ProductFixtures.activeProduct();

            // when
            ProductJpaEntity entity = mapper.toEntity(original);
            Product converted = mapper.toDomain(entity, List.of());

            // then
            assertThat(converted.idValue()).isEqualTo(original.idValue());
            assertThat(converted.productGroupIdValue()).isEqualTo(original.productGroupIdValue());
            assertThat(converted.regularPriceValue()).isEqualTo(original.regularPriceValue());
            assertThat(converted.currentPriceValue()).isEqualTo(original.currentPriceValue());
            assertThat(converted.discountRate()).isEqualTo(original.discountRate());
            assertThat(converted.stockQuantity()).isEqualTo(original.stockQuantity());
            assertThat(converted.status()).isEqualTo(original.status());
            assertThat(converted.sortOrder()).isEqualTo(original.sortOrder());
        }
    }
}
