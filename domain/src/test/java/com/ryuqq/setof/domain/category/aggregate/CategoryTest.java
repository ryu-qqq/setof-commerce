package com.ryuqq.setof.domain.category.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.category.CategoryFixtures;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryDepth;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Category Aggregate 테스트")
class CategoryTest {

    @Nested
    @DisplayName("forNewRoot() - 루트 카테고리 생성")
    class ForNewRootTest {

        @Test
        @DisplayName("루트 카테고리를 생성한다")
        void createNewRootCategory() {
            // when
            var category = CategoryFixtures.newRootCategory();

            // then
            assertThat(category.isNew()).isTrue();
            assertThat(category.isRoot()).isTrue();
            assertThat(category.categoryNameValue())
                    .isEqualTo(CategoryFixtures.DEFAULT_CATEGORY_NAME);
            assertThat(category.displayNameValue())
                    .isEqualTo(CategoryFixtures.DEFAULT_DISPLAY_NAME);
            assertThat(category.categoryDepthValue()).isEqualTo(1);
            assertThat(category.parentCategoryIdValue()).isEqualTo(0L);
            assertThat(category.isDisplayed()).isTrue();
            assertThat(category.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("커스텀 값으로 루트 카테고리를 생성한다")
        void createWithCustomValues() {
            // when
            var category = CategoryFixtures.newRootCategory("커스텀카테고리", "커스텀 표시명");

            // then
            assertThat(category.categoryNameValue()).isEqualTo("커스텀카테고리");
            assertThat(category.displayNameValue()).isEqualTo("커스텀 표시명");
        }
    }

    @Nested
    @DisplayName("forNewChild() - 하위 카테고리 생성")
    class ForNewChildTest {

        @Test
        @DisplayName("하위 카테고리를 생성한다")
        void createNewChildCategory() {
            // given
            CategoryId parentId = CategoryId.of(1L);
            CategoryDepth depth = CategoryDepth.of(2);
            String path = "/1/2";

            // when
            var category = CategoryFixtures.newChildCategory(parentId, depth, path);

            // then
            assertThat(category.isNew()).isTrue();
            assertThat(category.isRoot()).isFalse();
            assertThat(category.parentCategoryIdValue()).isEqualTo(1L);
            assertThat(category.categoryDepthValue()).isEqualTo(2);
            assertThat(category.pathValue()).isEqualTo(path);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태의 카테고리를 복원한다")
        void reconstituteActiveCategory() {
            // when
            var category = CategoryFixtures.activeCategory();

            // then
            assertThat(category.isNew()).isFalse();
            assertThat(category.idValue()).isEqualTo(1L);
            assertThat(category.isDisplayed()).isTrue();
            assertThat(category.isDeleted()).isFalse();
            assertThat(category.deletedAt()).isNull();
        }

        @Test
        @DisplayName("삭제된 카테고리를 복원한다")
        void reconstituteDeletedCategory() {
            // when
            var category = CategoryFixtures.deletedCategory();

            // then
            assertThat(category.isDeleted()).isTrue();
            assertThat(category.deletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("update() - 카테고리 수정")
    class UpdateTest {

        @Test
        @DisplayName("카테고리 정보를 수정한다")
        void updateCategory() {
            // given
            var category = CategoryFixtures.activeCategory();
            var updateData = CategoryFixtures.categoryUpdateData(false);
            Instant now = CommonVoFixtures.now();

            // when
            category.update(updateData, now);

            // then
            assertThat(category.isDisplayed()).isFalse();
            assertThat(category.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("show() / hide() - 표시 상태 변경")
    class DisplayTest {

        @Test
        @DisplayName("비표시 상태의 카테고리를 표시한다")
        void showCategory() {
            // given
            var category = CategoryFixtures.inactiveCategory();
            Instant now = CommonVoFixtures.now();

            // when
            category.show(now);

            // then
            assertThat(category.isDisplayed()).isTrue();
            assertThat(category.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("표시 상태의 카테고리를 비표시한다")
        void hideCategory() {
            // given
            var category = CategoryFixtures.activeCategory();
            Instant now = CommonVoFixtures.now();

            // when
            category.hide(now);

            // then
            assertThat(category.isDisplayed()).isFalse();
            assertThat(category.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("delete() / restore() - 삭제 및 복원")
    class DeletionTest {

        @Test
        @DisplayName("카테고리를 삭제(Soft Delete)한다")
        void deleteCategory() {
            // given
            var category = CategoryFixtures.activeCategory();
            Instant now = CommonVoFixtures.now();

            // when
            category.delete(now);

            // then
            assertThat(category.isDeleted()).isTrue();
            assertThat(category.deletedAt()).isEqualTo(now);
            assertThat(category.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("삭제된 카테고리를 복원한다")
        void restoreCategory() {
            // given
            var category = CategoryFixtures.deletedCategory();
            Instant now = CommonVoFixtures.now();

            // when
            category.restore(now);

            // then
            assertThat(category.isDeleted()).isFalse();
            assertThat(category.deletedAt()).isNull();
            assertThat(category.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("id()는 CategoryId를 반환한다")
        void returnsId() {
            var category = CategoryFixtures.activeCategory();
            assertThat(category.id()).isNotNull();
            assertThat(category.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("categoryName()은 CategoryName을 반환한다")
        void returnsCategoryName() {
            var category = CategoryFixtures.activeCategory();
            assertThat(category.categoryName()).isNotNull();
            assertThat(category.categoryNameValue())
                    .isEqualTo(CategoryFixtures.DEFAULT_CATEGORY_NAME);
        }

        @Test
        @DisplayName("targetGroup()은 TargetGroup을 반환한다")
        void returnsTargetGroup() {
            var category = CategoryFixtures.activeCategory();
            assertThat(category.targetGroup()).isNotNull();
        }

        @Test
        @DisplayName("categoryType()은 CategoryType을 반환한다")
        void returnsCategoryType() {
            var category = CategoryFixtures.activeCategory();
            assertThat(category.categoryType()).isNotNull();
        }

        @Test
        @DisplayName("path()는 CategoryPath를 반환한다")
        void returnsPath() {
            var category = CategoryFixtures.activeCategory();
            assertThat(category.path()).isNotNull();
            assertThat(category.pathValue()).isEqualTo("/1");
        }

        @Test
        @DisplayName("deletionStatus()는 DeletionStatus를 반환한다")
        void returnsDeletionStatus() {
            var category = CategoryFixtures.activeCategory();
            assertThat(category.deletionStatus()).isNotNull();
            assertThat(category.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("createdAt()은 생성 시각을 반환한다")
        void returnsCreatedAt() {
            var category = CategoryFixtures.activeCategory();
            assertThat(category.createdAt()).isNotNull();
        }
    }
}
