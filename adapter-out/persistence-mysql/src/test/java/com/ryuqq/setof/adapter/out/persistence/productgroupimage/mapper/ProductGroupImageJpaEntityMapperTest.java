package com.ryuqq.setof.adapter.out.persistence.productgroupimage.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.productgroupimage.ProductGroupImageJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.ProductGroupImageJpaEntity;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductGroupImageJpaEntityMapperTest - 상품그룹 이미지 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductGroupImageJpaEntityMapper 단위 테스트")
class ProductGroupImageJpaEntityMapperTest {

    private ProductGroupImageJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductGroupImageJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("썸네일 이미지 Domain을 Entity로 변환합니다")
        void toEntity_WithThumbnailImage_ConvertsCorrectly() {
            // given
            ProductGroupImage image =
                    com.setof.commerce.domain.productgroupimage.ProductGroupImageFixtures
                            .persistedThumbnailImage();
            Long productGroupId = 1L;

            // when
            ProductGroupImageJpaEntity entity = mapper.toEntity(image, productGroupId);

            // then
            assertThat(entity.getId()).isEqualTo(image.idValue());
            assertThat(entity.getProductGroupId()).isEqualTo(productGroupId);
            assertThat(entity.getImageType()).isEqualTo(image.imageType().name());
            assertThat(entity.getImageUrl()).isEqualTo(image.imageUrlValue());
            assertThat(entity.getSortOrder()).isEqualTo(image.sortOrder());
        }

        @Test
        @DisplayName("상세 이미지 Domain을 Entity로 변환합니다")
        void toEntity_WithDetailImage_ConvertsCorrectly() {
            // given
            ProductGroupImage image =
                    com.setof.commerce.domain.productgroupimage.ProductGroupImageFixtures
                            .persistedDetailImage();
            Long productGroupId = 2L;

            // when
            ProductGroupImageJpaEntity entity = mapper.toEntity(image, productGroupId);

            // then
            assertThat(entity.getImageType()).isEqualTo(ImageType.DETAIL.name());
            assertThat(entity.getProductGroupId()).isEqualTo(productGroupId);
        }

        @Test
        @DisplayName("소프트 삭제된 이미지 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedImage_PreservesDeletedAt() {
            // given
            ProductGroupImage image =
                    com.setof.commerce.domain.productgroupimage.ProductGroupImageFixtures
                            .deletedThumbnailImage();
            Long productGroupId = 1L;

            // when
            ProductGroupImageJpaEntity entity = mapper.toEntity(image, productGroupId);

            // then
            assertThat(entity.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("외부에서 전달된 productGroupId가 Entity에 반영됩니다")
        void toEntity_ProductGroupIdIsInjectedExternally() {
            // given
            ProductGroupImage image =
                    com.setof.commerce.domain.productgroupimage.ProductGroupImageFixtures
                            .newThumbnailImage();
            Long productGroupId = 999L;

            // when
            ProductGroupImageJpaEntity entity = mapper.toEntity(image, productGroupId);

            // then
            assertThat(entity.getProductGroupId()).isEqualTo(productGroupId);
        }
    }

    // ========================================================================
    // 2. toDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("썸네일 이미지 Entity를 Domain으로 변환합니다")
        void toDomain_WithThumbnailEntity_ConvertsCorrectly() {
            // given
            ProductGroupImageJpaEntity entity =
                    ProductGroupImageJpaEntityFixtures.thumbnailEntity();

            // when
            ProductGroupImage domain = mapper.toDomain(entity);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.imageType().name()).isEqualTo(entity.getImageType());
            assertThat(domain.imageUrlValue()).isEqualTo(entity.getImageUrl());
            assertThat(domain.sortOrder()).isEqualTo(entity.getSortOrder());
            assertThat(domain.deletedAt()).isNull();
        }

        @Test
        @DisplayName("상세 이미지 Entity를 Domain으로 변환합니다")
        void toDomain_WithDetailEntity_ConvertsCorrectly() {
            // given
            ProductGroupImageJpaEntity entity = ProductGroupImageJpaEntityFixtures.detailEntity();

            // when
            ProductGroupImage domain = mapper.toDomain(entity);

            // then
            assertThat(domain.imageType()).isEqualTo(ImageType.DETAIL);
            assertThat(domain.imageUrlValue()).isEqualTo(entity.getImageUrl());
        }

        @Test
        @DisplayName("소프트 삭제된 이미지 Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ConvertsCorrectly() {
            // given
            ProductGroupImageJpaEntity entity =
                    ProductGroupImageJpaEntityFixtures.deletedThumbnailEntity();

            // when
            ProductGroupImage domain = mapper.toDomain(entity);

            // then
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
        @DisplayName("Entity -> Domain -> Entity 변환 시 이미지 타입과 URL이 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            // given
            ProductGroupImageJpaEntity original =
                    ProductGroupImageJpaEntityFixtures.thumbnailEntity();
            Long productGroupId = original.getProductGroupId();

            // when
            ProductGroupImage domain = mapper.toDomain(original);
            ProductGroupImageJpaEntity converted = mapper.toEntity(domain, productGroupId);

            // then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getProductGroupId()).isEqualTo(original.getProductGroupId());
            assertThat(converted.getImageType()).isEqualTo(original.getImageType());
            assertThat(converted.getImageUrl()).isEqualTo(original.getImageUrl());
            assertThat(converted.getSortOrder()).isEqualTo(original.getSortOrder());
        }
    }
}
