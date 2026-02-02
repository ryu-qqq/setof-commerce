package com.ryuqq.setof.integration.test.repository.category;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.category.CategoryJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryJpaRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Category JPA Repository 통합 테스트.
 *
 * <p>JPA Repository의 기본 CRUD 동작을 검증합니다.
 */
@Tag(TestTags.CATEGORY)
@DisplayName("카테고리 JPA Repository 테스트")
class CategoryRepositoryTest extends RepositoryTestBase {

    @Autowired private CategoryJpaRepository categoryRepository;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("새 카테고리 저장 성공")
        void shouldSaveNewCategory() {
            // given
            CategoryJpaEntity entity = CategoryJpaEntityFixtures.newEntity();

            // when
            CategoryJpaEntity saved = categoryRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(find(CategoryJpaEntity.class, saved.getId())).isNotNull();
        }

        @Test
        @DisplayName("카테고리 정보가 올바르게 저장됩니다")
        void shouldSaveCategoryWithCorrectInfo() {
            // given
            CategoryJpaEntity entity = CategoryJpaEntityFixtures.newEntity();

            // when
            CategoryJpaEntity saved = categoryRepository.save(entity);
            flushAndClear();

            // then
            CategoryJpaEntity found = find(CategoryJpaEntity.class, saved.getId());
            assertThat(found.getCategoryName())
                    .isEqualTo(CategoryJpaEntityFixtures.DEFAULT_CATEGORY_NAME);
            assertThat(found.getDisplayName())
                    .isEqualTo(CategoryJpaEntityFixtures.DEFAULT_DISPLAY_NAME);
            assertThat(found.getCategoryDepth()).isEqualTo(CategoryJpaEntityFixtures.DEFAULT_DEPTH);
            assertThat(found.getParentCategoryId())
                    .isEqualTo(CategoryJpaEntityFixtures.DEFAULT_PARENT_ID);
            assertThat(found.isDisplayed()).isTrue();
        }

        @Test
        @DisplayName("자식 카테고리 저장 성공")
        void shouldSaveChildCategory() {
            // given
            CategoryJpaEntity parentEntity = CategoryJpaEntityFixtures.newRootEntity();
            CategoryJpaEntity savedParent = categoryRepository.save(parentEntity);
            flushAndClear();

            CategoryJpaEntity childEntity =
                    CategoryJpaEntityFixtures.childEntity(null, savedParent.getId(), 2);

            // when
            CategoryJpaEntity savedChild = categoryRepository.save(childEntity);
            flushAndClear();

            // then
            CategoryJpaEntity found = find(CategoryJpaEntity.class, savedChild.getId());
            assertThat(found.getParentCategoryId()).isEqualTo(savedParent.getId());
            assertThat(found.getCategoryDepth()).isEqualTo(2);
        }

        @Test
        @DisplayName("비활성 카테고리 저장 성공")
        void shouldSaveInactiveCategory() {
            // given
            CategoryJpaEntity entity = CategoryJpaEntityFixtures.newInactiveEntity();

            // when
            CategoryJpaEntity saved = categoryRepository.save(entity);
            flushAndClear();

            // then
            CategoryJpaEntity found = find(CategoryJpaEntity.class, saved.getId());
            assertThat(found.isDisplayed()).isFalse();
        }
    }
}
