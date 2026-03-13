package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.ProductGroupDescriptionJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.DescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.ProductGroupDescriptionJpaEntity;
import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductGroupDescriptionJpaEntityMapperTest - 상품그룹 상세설명 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ProductGroupDescriptionJpaEntityMapper 단위 테스트")
class ProductGroupDescriptionJpaEntityMapperTest {

    private ProductGroupDescriptionJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductGroupDescriptionJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveDescription_ConvertsCorrectly() {
            // given
            ProductGroupDescription domain =
                    com.setof.commerce.domain.productdescription.ProductDescriptionFixtures
                            .activeDescription();

            // when
            ProductGroupDescriptionJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getProductGroupId()).isEqualTo(domain.productGroupIdValue());
            assertThat(entity.getContent()).isEqualTo(domain.contentValue());
            assertThat(entity.getCdnPath()).isEqualTo(domain.cdnPath());
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("content가 null인 Domain을 Entity로 변환합니다")
        void toEntity_WithNullContent_ConvertsCorrectly() {
            // given
            ProductGroupDescription domain =
                    com.setof.commerce.domain.productdescription.ProductDescriptionFixtures
                            .emptyDescription();

            // when
            ProductGroupDescriptionJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getContent()).isNull();
            assertThat(entity.getCdnPath()).isNull();
        }

        @Test
        @DisplayName("신규 Domain을 Entity로 변환합니다 (ID가 null)")
        void toEntity_WithNewDescription_IdIsNull() {
            // given
            ProductGroupDescription domain =
                    com.setof.commerce.domain.productdescription.ProductDescriptionFixtures
                            .newDescription();

            // when
            ProductGroupDescriptionJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isNull();
        }
    }

    // ========================================================================
    // 2. toImageEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toImageEntity 메서드 테스트")
    class ToImageEntityTest {

        @Test
        @DisplayName("DescriptionImage Domain을 Entity로 변환합니다")
        void toImageEntity_WithValidImage_ConvertsCorrectly() {
            // given
            DescriptionImage image =
                    com.setof.commerce.domain.productdescription.ProductDescriptionFixtures
                            .descriptionImage();
            Long productGroupDescriptionId = 1L;

            // when
            DescriptionImageJpaEntity entity =
                    mapper.toImageEntity(image, productGroupDescriptionId);

            // then
            assertThat(entity.getProductGroupDescriptionId()).isEqualTo(productGroupDescriptionId);
            assertThat(entity.getImageUrl()).isEqualTo(image.imageUrl());
            assertThat(entity.getSortOrder()).isEqualTo(image.sortOrder());
        }

        @Test
        @DisplayName("외부에서 전달된 descriptionId가 Entity에 반영됩니다")
        void toImageEntity_DescriptionIdIsInjectedExternally() {
            // given
            DescriptionImage image =
                    com.setof.commerce.domain.productdescription.ProductDescriptionFixtures
                            .descriptionImage(2);
            Long productGroupDescriptionId = 777L;

            // when
            DescriptionImageJpaEntity entity =
                    mapper.toImageEntity(image, productGroupDescriptionId);

            // then
            assertThat(entity.getProductGroupDescriptionId()).isEqualTo(productGroupDescriptionId);
            assertThat(entity.getSortOrder()).isEqualTo(2);
        }
    }

    // ========================================================================
    // 3. toDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("기본 상세설명 Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveEntity_ConvertsCorrectly() {
            // given
            ProductGroupDescriptionJpaEntity entity =
                    ProductGroupDescriptionJpaEntityFixtures.activeEntity();
            List<DescriptionImageJpaEntity> imageEntities =
                    ProductGroupDescriptionJpaEntityFixtures.defaultImageEntities();

            // when
            ProductGroupDescription domain = mapper.toDomain(entity, imageEntities);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.productGroupIdValue()).isEqualTo(entity.getProductGroupId());
            assertThat(domain.contentValue()).isEqualTo(entity.getContent());
            assertThat(domain.cdnPath()).isEqualTo(entity.getCdnPath());
            assertThat(domain.images()).hasSize(3);
        }

        @Test
        @DisplayName("이미지가 없는 상세설명 Entity를 Domain으로 변환합니다")
        void toDomain_WithNoImages_ConvertsCorrectly() {
            // given
            ProductGroupDescriptionJpaEntity entity =
                    ProductGroupDescriptionJpaEntityFixtures.activeEntity();
            List<DescriptionImageJpaEntity> imageEntities =
                    ProductGroupDescriptionJpaEntityFixtures.emptyImageEntities();

            // when
            ProductGroupDescription domain = mapper.toDomain(entity, imageEntities);

            // then
            assertThat(domain.images()).isEmpty();
        }

        @Test
        @DisplayName("content가 null인 Entity를 Domain으로 변환합니다")
        void toDomain_WithNullContent_ConvertsCorrectly() {
            // given
            ProductGroupDescriptionJpaEntity entity =
                    ProductGroupDescriptionJpaEntityFixtures.entityWithNullContent();

            // when
            ProductGroupDescription domain = mapper.toDomain(entity, List.of());

            // then
            assertThat(domain.contentValue()).isNull();
            assertThat(domain.cdnPath()).isNull();
        }
    }

    // ========================================================================
    // 4. toImageDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toImageDomain 메서드 테스트")
    class ToImageDomainTest {

        @Test
        @DisplayName("DescriptionImageJpaEntity를 DescriptionImage Domain으로 변환합니다")
        void toImageDomain_WithValidEntity_ConvertsCorrectly() {
            // given
            ProductGroupDescriptionJpaEntity descEntity =
                    ProductGroupDescriptionJpaEntityFixtures.activeEntity();
            List<DescriptionImageJpaEntity> imageEntities =
                    ProductGroupDescriptionJpaEntityFixtures.defaultImageEntities();

            // when
            ProductGroupDescription domain = mapper.toDomain(descEntity, imageEntities);

            // then - 첫 번째 이미지가 올바르게 변환됐는지 검증
            DescriptionImage firstImage = domain.images().get(0);
            assertThat(firstImage.imageUrl()).isEqualTo("https://cdn.example.com/desc/image1.jpg");
            assertThat(firstImage.sortOrder()).isEqualTo(0);
        }
    }

    // ========================================================================
    // 5. 양방향 변환 테스트
    // ========================================================================

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            // given
            ProductGroupDescriptionJpaEntity original =
                    ProductGroupDescriptionJpaEntityFixtures.activeEntity();

            // when
            ProductGroupDescription domain = mapper.toDomain(original, List.of());
            ProductGroupDescriptionJpaEntity converted = mapper.toEntity(domain);

            // then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getProductGroupId()).isEqualTo(original.getProductGroupId());
            assertThat(converted.getContent()).isEqualTo(original.getContent());
            assertThat(converted.getCdnPath()).isEqualTo(original.getCdnPath());
        }
    }
}
