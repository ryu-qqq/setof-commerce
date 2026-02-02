package com.ryuqq.setof.adapter.out.persistence.brand.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.brand.BrandJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.domain.brand.BrandFixtures;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * BrandJpaEntityMapperTest - 브랜드 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("BrandJpaEntityMapper 단위 테스트")
class BrandJpaEntityMapperTest {

    private BrandJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BrandJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveBrand_ConvertsCorrectly() {
            // given
            Brand domain = BrandFixtures.activeBrand();

            // when
            BrandJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getBrandName()).isEqualTo(domain.brandNameValue());
            assertThat(entity.getBrandIconImageUrl()).isEqualTo(domain.brandIconImageUrlValue());
            assertThat(entity.getDisplayName()).isEqualTo(domain.displayNameValue());
            assertThat(entity.getDisplayOrder()).isEqualTo(domain.displayOrderValue());
            assertThat(entity.isDisplayed()).isTrue();
        }

        @Test
        @DisplayName("비활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithInactiveBrand_ConvertsCorrectly() {
            // given
            Brand domain = BrandFixtures.inactiveBrand();

            // when
            BrandJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.isDisplayed()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedBrand_ConvertsCorrectly() {
            // given
            Brand domain = BrandFixtures.deletedBrand();

            // when
            BrandJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("새로운 Domain을 Entity로 변환합니다")
        void toEntity_WithNewBrand_ConvertsCorrectly() {
            // given
            Brand domain = BrandFixtures.newBrand();

            // when
            BrandJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isNull();
            assertThat(entity.getBrandName()).isEqualTo(domain.brandNameValue());
        }
    }

    // ========================================================================
    // 2. toDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("활성 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveEntity_ConvertsCorrectly() {
            // given
            BrandJpaEntity entity = BrandJpaEntityFixtures.activeEntity();

            // when
            Brand domain = mapper.toDomain(entity);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.brandNameValue()).isEqualTo(entity.getBrandName());
            assertThat(domain.brandIconImageUrlValue()).isEqualTo(entity.getBrandIconImageUrl());
            assertThat(domain.displayNameValue()).isEqualTo(entity.getDisplayName());
            assertThat(domain.displayOrderValue()).isEqualTo(entity.getDisplayOrder());
            assertThat(domain.isDisplayed()).isTrue();
        }

        @Test
        @DisplayName("비활성 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithInactiveEntity_ConvertsCorrectly() {
            // given
            BrandJpaEntity entity = BrandJpaEntityFixtures.inactiveEntity();

            // when
            Brand domain = mapper.toDomain(entity);

            // then
            assertThat(domain.isDisplayed()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ConvertsCorrectly() {
            // given
            BrandJpaEntity entity = BrandJpaEntityFixtures.deletedEntity();

            // when
            Brand domain = mapper.toDomain(entity);

            // then
            assertThat(domain.isDeleted()).isTrue();
            assertThat(domain.deletedAt()).isNotNull();
        }
    }

    // ========================================================================
    // 3. 양방향 변환 테스트
    // ========================================================================

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Domain -> Entity -> Domain 변환 시 데이터가 보존됩니다")
        void roundTrip_DomainToEntityToDomain_PreservesData() {
            // given
            Brand original = BrandFixtures.activeBrand();

            // when
            BrandJpaEntity entity = mapper.toEntity(original);
            Brand converted = mapper.toDomain(entity);

            // then
            assertThat(converted.idValue()).isEqualTo(original.idValue());
            assertThat(converted.brandNameValue()).isEqualTo(original.brandNameValue());
            assertThat(converted.brandIconImageUrlValue())
                    .isEqualTo(original.brandIconImageUrlValue());
            assertThat(converted.displayNameValue()).isEqualTo(original.displayNameValue());
            assertThat(converted.displayOrderValue()).isEqualTo(original.displayOrderValue());
            assertThat(converted.isDisplayed()).isEqualTo(original.isDisplayed());
        }

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            // given
            BrandJpaEntity original = BrandJpaEntityFixtures.activeEntity();

            // when
            Brand domain = mapper.toDomain(original);
            BrandJpaEntity converted = mapper.toEntity(domain);

            // then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getBrandName()).isEqualTo(original.getBrandName());
            assertThat(converted.getBrandIconImageUrl()).isEqualTo(original.getBrandIconImageUrl());
            assertThat(converted.getDisplayName()).isEqualTo(original.getDisplayName());
            assertThat(converted.getDisplayOrder()).isEqualTo(original.getDisplayOrder());
            assertThat(converted.isDisplayed()).isEqualTo(original.isDisplayed());
        }
    }
}
