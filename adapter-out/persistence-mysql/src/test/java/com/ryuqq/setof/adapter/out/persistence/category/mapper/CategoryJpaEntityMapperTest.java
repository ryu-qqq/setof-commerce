package com.ryuqq.setof.adapter.out.persistence.category.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.category.CategoryJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.category.dto.CategoryTreeDto;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.domain.category.CategoryFixtures;
import com.ryuqq.setof.domain.category.aggregate.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * CategoryJpaEntityMapperTest - 카테고리 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CategoryJpaEntityMapper 단위 테스트")
class CategoryJpaEntityMapperTest {

    private CategoryJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CategoryJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveCategory_ConvertsCorrectly() {
            // given
            Category domain = CategoryFixtures.activeCategory();

            // when
            CategoryJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getCategoryName()).isEqualTo(domain.categoryNameValue());
            assertThat(entity.getCategoryDepth()).isEqualTo(domain.categoryDepthValue());
            assertThat(entity.getParentCategoryId()).isEqualTo(domain.parentCategoryIdValue());
            assertThat(entity.getDisplayName()).isEqualTo(domain.displayNameValue());
            assertThat(entity.isDisplayed()).isTrue();
            assertThat(entity.getTargetGroup()).isEqualTo(domain.targetGroup());
            assertThat(entity.getCategoryType()).isEqualTo(domain.categoryType());
            assertThat(entity.getPath()).isEqualTo(domain.pathValue());
        }

        @Test
        @DisplayName("비활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithInactiveCategory_ConvertsCorrectly() {
            // given
            Category domain = CategoryFixtures.inactiveCategory();

            // when
            CategoryJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.isDisplayed()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedCategory_ConvertsCorrectly() {
            // given
            Category domain = CategoryFixtures.deletedCategory();

            // when
            CategoryJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("새로운 루트 Domain을 Entity로 변환합니다")
        void toEntity_WithNewRootCategory_ConvertsCorrectly() {
            // given
            Category domain = CategoryFixtures.newRootCategory();

            // when
            CategoryJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isNull();
            assertThat(entity.getCategoryName()).isEqualTo(domain.categoryNameValue());
        }

        @Test
        @DisplayName("자식 카테고리 Domain을 Entity로 변환합니다")
        void toEntity_WithChildCategory_ConvertsCorrectly() {
            // given
            Category domain = CategoryFixtures.activeChildCategory(10L, 1L);

            // when
            CategoryJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getParentCategoryId()).isEqualTo(1L);
            assertThat(entity.getCategoryDepth()).isEqualTo(2);
        }
    }

    // ========================================================================
    // 2. toDomain (Entity) 테스트
    // ========================================================================

    @Nested
    @DisplayName("toDomain (Entity) 메서드 테스트")
    class ToDomainEntityTest {

        @Test
        @DisplayName("활성 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveEntity_ConvertsCorrectly() {
            // given
            CategoryJpaEntity entity = CategoryJpaEntityFixtures.activeEntity();

            // when
            Category domain = mapper.toDomain(entity);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.categoryNameValue()).isEqualTo(entity.getCategoryName());
            assertThat(domain.categoryDepthValue()).isEqualTo(entity.getCategoryDepth());
            assertThat(domain.parentCategoryIdValue()).isEqualTo(entity.getParentCategoryId());
            assertThat(domain.displayNameValue()).isEqualTo(entity.getDisplayName());
            assertThat(domain.isDisplayed()).isTrue();
            assertThat(domain.targetGroup()).isEqualTo(entity.getTargetGroup());
            assertThat(domain.categoryType()).isEqualTo(entity.getCategoryType());
            assertThat(domain.pathValue()).isEqualTo(entity.getPath());
        }

        @Test
        @DisplayName("비활성 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithInactiveEntity_ConvertsCorrectly() {
            // given
            CategoryJpaEntity entity = CategoryJpaEntityFixtures.inactiveEntity();

            // when
            Category domain = mapper.toDomain(entity);

            // then
            assertThat(domain.isDisplayed()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ConvertsCorrectly() {
            // given
            CategoryJpaEntity entity = CategoryJpaEntityFixtures.deletedEntity();

            // when
            Category domain = mapper.toDomain(entity);

            // then
            assertThat(domain.isDeleted()).isTrue();
            assertThat(domain.deletedAt()).isNotNull();
        }

        @Test
        @DisplayName("자식 카테고리 Entity를 Domain으로 변환합니다")
        void toDomain_WithChildEntity_ConvertsCorrectly() {
            // given
            CategoryJpaEntity entity = CategoryJpaEntityFixtures.childEntity(10L, 1L, 2);

            // when
            Category domain = mapper.toDomain(entity);

            // then
            assertThat(domain.parentCategoryIdValue()).isEqualTo(1L);
            assertThat(domain.categoryDepthValue()).isEqualTo(2);
        }
    }

    // ========================================================================
    // 3. toDomain (TreeDto) 테스트
    // ========================================================================

    @Nested
    @DisplayName("toDomain (TreeDto) 메서드 테스트")
    class ToDomainTreeDtoTest {

        @Test
        @DisplayName("활성 상태 TreeDto를 Domain으로 변환합니다")
        void toDomain_WithActiveTreeDto_ConvertsCorrectly() {
            // given
            CategoryTreeDto dto = CategoryJpaEntityFixtures.activeTreeDto();

            // when
            Category domain = mapper.toDomain(dto);

            // then
            assertThat(domain.idValue()).isEqualTo(dto.getId());
            assertThat(domain.categoryNameValue()).isEqualTo(dto.getCategoryName());
            assertThat(domain.categoryDepthValue()).isEqualTo(dto.getCategoryDepth());
            assertThat(domain.parentCategoryIdValue()).isEqualTo(dto.getParentCategoryId());
            assertThat(domain.displayNameValue()).isEqualTo(dto.getDisplayName());
            assertThat(domain.isDisplayed()).isEqualTo(dto.isDisplayed());
            assertThat(domain.targetGroup()).isEqualTo(dto.getTargetGroup());
            assertThat(domain.categoryType()).isEqualTo(dto.getCategoryType());
            assertThat(domain.pathValue()).isEqualTo(dto.getPath());
        }

        @Test
        @DisplayName("자식 카테고리 TreeDto를 Domain으로 변환합니다")
        void toDomain_WithChildTreeDto_ConvertsCorrectly() {
            // given
            CategoryTreeDto dto = CategoryJpaEntityFixtures.treeDto(10L, 1L, 2);

            // when
            Category domain = mapper.toDomain(dto);

            // then
            assertThat(domain.parentCategoryIdValue()).isEqualTo(1L);
            assertThat(domain.categoryDepthValue()).isEqualTo(2);
        }
    }

    // ========================================================================
    // 4. 양방향 변환 테스트
    // ========================================================================

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Domain -> Entity -> Domain 변환 시 데이터가 보존됩니다")
        void roundTrip_DomainToEntityToDomain_PreservesData() {
            // given
            Category original = CategoryFixtures.activeCategory();

            // when
            CategoryJpaEntity entity = mapper.toEntity(original);
            Category converted = mapper.toDomain(entity);

            // then
            assertThat(converted.idValue()).isEqualTo(original.idValue());
            assertThat(converted.categoryNameValue()).isEqualTo(original.categoryNameValue());
            assertThat(converted.categoryDepthValue()).isEqualTo(original.categoryDepthValue());
            assertThat(converted.parentCategoryIdValue())
                    .isEqualTo(original.parentCategoryIdValue());
            assertThat(converted.displayNameValue()).isEqualTo(original.displayNameValue());
            assertThat(converted.isDisplayed()).isEqualTo(original.isDisplayed());
            assertThat(converted.targetGroup()).isEqualTo(original.targetGroup());
            assertThat(converted.categoryType()).isEqualTo(original.categoryType());
            assertThat(converted.pathValue()).isEqualTo(original.pathValue());
        }

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            // given
            CategoryJpaEntity original = CategoryJpaEntityFixtures.activeEntity();

            // when
            Category domain = mapper.toDomain(original);
            CategoryJpaEntity converted = mapper.toEntity(domain);

            // then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getCategoryName()).isEqualTo(original.getCategoryName());
            assertThat(converted.getCategoryDepth()).isEqualTo(original.getCategoryDepth());
            assertThat(converted.getParentCategoryId()).isEqualTo(original.getParentCategoryId());
            assertThat(converted.getDisplayName()).isEqualTo(original.getDisplayName());
            assertThat(converted.isDisplayed()).isEqualTo(original.isDisplayed());
            assertThat(converted.getTargetGroup()).isEqualTo(original.getTargetGroup());
            assertThat(converted.getCategoryType()).isEqualTo(original.getCategoryType());
            assertThat(converted.getPath()).isEqualTo(original.getPath());
        }
    }
}
