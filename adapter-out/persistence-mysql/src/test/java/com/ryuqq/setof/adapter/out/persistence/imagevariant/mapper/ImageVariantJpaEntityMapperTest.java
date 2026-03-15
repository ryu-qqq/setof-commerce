package com.ryuqq.setof.adapter.out.persistence.imagevariant.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.ImageVariantJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.ImageVariantJpaEntity;
import com.ryuqq.setof.domain.imagevariant.ImageVariantFixtures;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ImageVariantJpaEntityMapperTest - 이미지 Variant Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ImageVariantJpaEntityMapper 단위 테스트")
class ImageVariantJpaEntityMapperTest {

    private ImageVariantJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ImageVariantJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveImageVariant_ConvertsCorrectly() {
            // given
            ImageVariant domain = ImageVariantFixtures.activeImageVariant();

            // when
            ImageVariantJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getSourceImageId()).isEqualTo(domain.sourceImageId());
            assertThat(entity.getSourceType()).isEqualTo(domain.sourceType().name());
            assertThat(entity.getVariantType()).isEqualTo(domain.variantType().name());
            assertThat(entity.getResultAssetId()).isEqualTo(domain.resultAssetIdValue());
            assertThat(entity.getVariantUrl()).isEqualTo(domain.variantUrlValue());
            assertThat(entity.getWidth()).isEqualTo(domain.width());
            assertThat(entity.getHeight()).isEqualTo(domain.height());
        }

        @Test
        @DisplayName("삭제된 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedImageVariant_ConvertsCorrectly() {
            // given
            ImageVariant domain = ImageVariantFixtures.deletedImageVariant();

            // when
            ImageVariantJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("새로운 Domain을 Entity로 변환합니다")
        void toEntity_WithNewImageVariant_ConvertsCorrectly() {
            // given
            ImageVariant domain = ImageVariantFixtures.newImageVariant();

            // when
            ImageVariantJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getSourceImageId()).isEqualTo(domain.sourceImageId());
            assertThat(entity.getSourceType()).isEqualTo(domain.sourceType().name());
            assertThat(entity.getVariantType()).isEqualTo(domain.variantType().name());
            assertThat(entity.getResultAssetId()).isEqualTo(domain.resultAssetIdValue());
            assertThat(entity.getVariantUrl()).isEqualTo(domain.variantUrlValue());
        }

        @Test
        @DisplayName("dimension이 null인 Domain을 Entity로 변환합니다")
        void toEntity_WithNullDimension_ConvertsCorrectly() {
            // given
            ImageVariant domain = ImageVariantFixtures.originalWebpVariant();

            // when
            ImageVariantJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getWidth()).isNull();
            assertThat(entity.getHeight()).isNull();
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
            ImageVariantJpaEntity entity = ImageVariantJpaEntityFixtures.activeEntity();

            // when
            ImageVariant domain = mapper.toDomain(entity);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.sourceImageId()).isEqualTo(entity.getSourceImageId());
            assertThat(domain.sourceType().name()).isEqualTo(entity.getSourceType());
            assertThat(domain.variantType().name()).isEqualTo(entity.getVariantType());
            assertThat(domain.resultAssetIdValue()).isEqualTo(entity.getResultAssetId());
            assertThat(domain.variantUrlValue()).isEqualTo(entity.getVariantUrl());
            assertThat(domain.width()).isEqualTo(entity.getWidth());
            assertThat(domain.height()).isEqualTo(entity.getHeight());
            assertThat(domain.deletedAt()).isNull();
        }

        @Test
        @DisplayName("삭제된 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ConvertsCorrectly() {
            // given
            ImageVariantJpaEntity entity = ImageVariantJpaEntityFixtures.deletedEntity();

            // when
            ImageVariant domain = mapper.toDomain(entity);

            // then
            assertThat(domain.isDeleted()).isTrue();
            assertThat(domain.deletedAt()).isNotNull();
        }

        @Test
        @DisplayName("dimension이 null인 Entity를 Domain으로 변환합니다")
        void toDomain_WithNullDimensionEntity_ConvertsCorrectly() {
            // given
            ImageVariantJpaEntity entity =
                    ImageVariantJpaEntityFixtures.newEntityWithNullDimension();

            // when
            ImageVariant domain = mapper.toDomain(entity);

            // then
            assertThat(domain.width()).isNull();
            assertThat(domain.height()).isNull();
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
            ImageVariant original = ImageVariantFixtures.activeImageVariant();

            // when
            ImageVariantJpaEntity entity = mapper.toEntity(original);
            ImageVariant converted = mapper.toDomain(entity);

            // then
            assertThat(converted.idValue()).isEqualTo(original.idValue());
            assertThat(converted.sourceImageId()).isEqualTo(original.sourceImageId());
            assertThat(converted.sourceType()).isEqualTo(original.sourceType());
            assertThat(converted.variantType()).isEqualTo(original.variantType());
            assertThat(converted.resultAssetIdValue()).isEqualTo(original.resultAssetIdValue());
            assertThat(converted.variantUrlValue()).isEqualTo(original.variantUrlValue());
            assertThat(converted.width()).isEqualTo(original.width());
            assertThat(converted.height()).isEqualTo(original.height());
        }

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            // given
            ImageVariantJpaEntity original = ImageVariantJpaEntityFixtures.activeEntity();

            // when
            ImageVariant domain = mapper.toDomain(original);
            ImageVariantJpaEntity converted = mapper.toEntity(domain);

            // then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getSourceImageId()).isEqualTo(original.getSourceImageId());
            assertThat(converted.getSourceType()).isEqualTo(original.getSourceType());
            assertThat(converted.getVariantType()).isEqualTo(original.getVariantType());
            assertThat(converted.getResultAssetId()).isEqualTo(original.getResultAssetId());
            assertThat(converted.getVariantUrl()).isEqualTo(original.getVariantUrl());
            assertThat(converted.getWidth()).isEqualTo(original.getWidth());
            assertThat(converted.getHeight()).isEqualTo(original.getHeight());
        }
    }
}
