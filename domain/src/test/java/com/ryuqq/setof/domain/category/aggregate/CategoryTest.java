package com.ryuqq.setof.domain.category.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.category.vo.CategoryCode;
import com.ryuqq.setof.domain.category.vo.CategoryDepth;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryName;
import com.ryuqq.setof.domain.category.vo.CategoryPath;
import com.ryuqq.setof.domain.category.vo.CategoryStatus;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Category Aggregate 테스트
 *
 * <p>Category는 조회 전용 엔티티로, reconstitute() 팩토리 메서드와 조회 관련 메서드만 테스트합니다.
 */
@DisplayName("Category Aggregate")
class CategoryTest {

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("최상위 카테고리를 복원할 수 있다")
        void shouldReconstituteRootCategory() {
            // given
            CategoryId id = CategoryId.of(1L);
            CategoryCode code = CategoryCode.of("FASHION");
            CategoryName nameKo = CategoryName.of("패션");
            Long parentId = null;
            CategoryDepth depth = CategoryDepth.of(0);
            CategoryPath path = CategoryPath.of("/1/");
            int sortOrder = 1;
            boolean isLeaf = false;
            CategoryStatus status = CategoryStatus.ACTIVE;

            // when
            Category category =
                    Category.reconstitute(
                            id, code, nameKo, parentId, depth, path, sortOrder, isLeaf, status, NOW,
                            NOW);

            // then
            assertNotNull(category);
            assertEquals(1L, category.getIdValue());
            assertEquals("FASHION", category.getCodeValue());
            assertEquals("패션", category.getNameKoValue());
            assertNull(category.getParentId());
            assertEquals(0, category.getDepthValue());
            assertEquals("/1/", category.getPathValue());
            assertEquals(1, category.getSortOrder());
            assertFalse(category.isLeaf());
            assertEquals("ACTIVE", category.getStatusValue());
        }

        @Test
        @DisplayName("하위 카테고리를 복원할 수 있다")
        void shouldReconstituteChildCategory() {
            // given
            CategoryId id = CategoryId.of(5L);
            CategoryCode code = CategoryCode.of("MENS");
            CategoryName nameKo = CategoryName.of("남성의류");
            Long parentId = 1L;
            CategoryDepth depth = CategoryDepth.of(1);
            CategoryPath path = CategoryPath.of("/1/5/");
            int sortOrder = 1;
            boolean isLeaf = false;
            CategoryStatus status = CategoryStatus.ACTIVE;

            // when
            Category category =
                    Category.reconstitute(
                            id, code, nameKo, parentId, depth, path, sortOrder, isLeaf, status, NOW,
                            NOW);

            // then
            assertNotNull(category);
            assertEquals(5L, category.getIdValue());
            assertEquals(1L, category.getParentId());
            assertEquals(1, category.getDepthValue());
            assertEquals("/1/5/", category.getPathValue());
            assertTrue(category.hasParent());
            assertFalse(category.isRoot());
        }

        @Test
        @DisplayName("리프 카테고리를 복원할 수 있다")
        void shouldReconstituteLeafCategory() {
            // given
            CategoryId id = CategoryId.of(23L);
            CategoryCode code = CategoryCode.of("MENS_TSHIRTS");
            CategoryName nameKo = CategoryName.of("남성 티셔츠");
            Long parentId = 5L;
            CategoryDepth depth = CategoryDepth.of(2);
            CategoryPath path = CategoryPath.of("/1/5/23/");
            int sortOrder = 1;
            boolean isLeaf = true;
            CategoryStatus status = CategoryStatus.ACTIVE;

            // when
            Category category =
                    Category.reconstitute(
                            id, code, nameKo, parentId, depth, path, sortOrder, isLeaf, status, NOW,
                            NOW);

            // then
            assertNotNull(category);
            assertTrue(category.isLeaf());
            assertTrue(category.isLeafNode());
        }

        @Test
        @DisplayName("비활성 카테고리를 복원할 수 있다")
        void shouldReconstituteInactiveCategory() {
            // given
            CategoryStatus status = CategoryStatus.INACTIVE;

            // when
            Category category = createCategoryWithStatus(status);

            // then
            assertNotNull(category);
            assertFalse(category.isActive());
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getIdValue()는 ID 값을 직접 반환한다")
        void shouldReturnIdValueDirectly() {
            // given
            Category category = createCategory();

            // then
            assertEquals(1L, category.getIdValue());
        }

        @Test
        @DisplayName("getCodeValue()는 카테고리 코드를 직접 반환한다")
        void shouldReturnCodeValueDirectly() {
            // given
            Category category = createCategory();

            // then
            assertEquals("FASHION", category.getCodeValue());
        }

        @Test
        @DisplayName("getNameKoValue()는 카테고리명을 직접 반환한다")
        void shouldReturnNameKoValueDirectly() {
            // given
            Category category = createCategory();

            // then
            assertEquals("패션", category.getNameKoValue());
        }

        @Test
        @DisplayName("getDepthValue()는 깊이를 직접 반환한다")
        void shouldReturnDepthValueDirectly() {
            // given
            Category category = createCategory();

            // then
            assertEquals(0, category.getDepthValue());
        }

        @Test
        @DisplayName("getPathValue()는 경로를 직접 반환한다")
        void shouldReturnPathValueDirectly() {
            // given
            Category category = createCategory();

            // then
            assertEquals("/1/", category.getPathValue());
        }

        @Test
        @DisplayName("getStatusValue()는 상태 이름을 직접 반환한다")
        void shouldReturnStatusValueDirectly() {
            // given
            Category category = createCategory();

            // then
            assertEquals("ACTIVE", category.getStatusValue());
        }
    }

    @Nested
    @DisplayName("비즈니스 메서드")
    class BusinessMethods {

        @Test
        @DisplayName("isActive()는 활성 카테고리이면 true를 반환한다")
        void shouldReturnTrueForActiveCategory() {
            // given
            Category category = createCategoryWithStatus(CategoryStatus.ACTIVE);

            // then
            assertTrue(category.isActive());
        }

        @Test
        @DisplayName("isActive()는 비활성 카테고리이면 false를 반환한다")
        void shouldReturnFalseForInactiveCategory() {
            // given
            Category category = createCategoryWithStatus(CategoryStatus.INACTIVE);

            // then
            assertFalse(category.isActive());
        }

        @Test
        @DisplayName("isRoot()는 최상위 카테고리이면 true를 반환한다")
        void shouldReturnTrueForRootCategory() {
            // given
            Category category = createCategory();

            // then
            assertTrue(category.isRoot());
        }

        @Test
        @DisplayName("isRoot()는 하위 카테고리이면 false를 반환한다")
        void shouldReturnFalseForChildCategory() {
            // given
            Category category = createChildCategory();

            // then
            assertFalse(category.isRoot());
        }

        @Test
        @DisplayName("hasParent()는 부모가 있으면 true를 반환한다")
        void shouldReturnTrueWhenHasParent() {
            // given
            Category category = createChildCategory();

            // then
            assertTrue(category.hasParent());
        }

        @Test
        @DisplayName("hasParent()는 부모가 없으면 false를 반환한다")
        void shouldReturnFalseWhenNoParent() {
            // given
            Category category = createCategory();

            // then
            assertFalse(category.hasParent());
        }

        @Test
        @DisplayName("isLeafNode()는 리프 카테고리이면 true를 반환한다")
        void shouldReturnTrueForLeafCategory() {
            // given
            Category category = createLeafCategory();

            // then
            assertTrue(category.isLeafNode());
        }

        @Test
        @DisplayName("isLeafNode()는 리프가 아니면 false를 반환한다")
        void shouldReturnFalseForNonLeafCategory() {
            // given
            Category category = createCategory();

            // then
            assertFalse(category.isLeafNode());
        }

        @Test
        @DisplayName("isDescendantOf()는 하위 경로이면 true를 반환한다")
        void shouldReturnTrueWhenDescendant() {
            // given
            Category category = createChildCategory();

            // then
            assertTrue(category.isDescendantOf(1L));
        }

        @Test
        @DisplayName("isDescendantOf()는 상위 경로가 아니면 false를 반환한다")
        void shouldReturnFalseWhenNotDescendant() {
            // given
            Category category = createChildCategory();

            // then
            assertFalse(category.isDescendantOf(99L));
        }
    }

    @Nested
    @DisplayName("Getter 메서드")
    class Getters {

        @Test
        @DisplayName("모든 Getter 메서드가 올바른 값을 반환한다")
        void shouldReturnCorrectValuesFromGetters() {
            // given
            CategoryId id = CategoryId.of(88L);
            CategoryCode code = CategoryCode.of("BEAUTY");
            CategoryName nameKo = CategoryName.of("뷰티");
            Long parentId = null;
            CategoryDepth depth = CategoryDepth.of(0);
            CategoryPath path = CategoryPath.of("/88/");
            int sortOrder = 5;
            boolean isLeaf = false;
            CategoryStatus status = CategoryStatus.ACTIVE;
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            Category category =
                    Category.reconstitute(
                            id, code, nameKo, parentId, depth, path, sortOrder, isLeaf, status,
                            createdAt, updatedAt);

            // then
            assertEquals(id, category.getId());
            assertEquals(code, category.getCode());
            assertEquals(nameKo, category.getNameKo());
            assertEquals(parentId, category.getParentId());
            assertEquals(depth, category.getDepth());
            assertEquals(path, category.getPath());
            assertEquals(sortOrder, category.getSortOrder());
            assertEquals(isLeaf, category.isLeaf());
            assertEquals(status, category.getStatus());
            assertEquals(createdAt, category.getCreatedAt());
            assertEquals(updatedAt, category.getUpdatedAt());
        }
    }

    // ========== Helper Methods ==========

    private Category createCategory() {
        return Category.reconstitute(
                CategoryId.of(1L),
                CategoryCode.of("FASHION"),
                CategoryName.of("패션"),
                null,
                CategoryDepth.of(0),
                CategoryPath.of("/1/"),
                1,
                false,
                CategoryStatus.ACTIVE,
                NOW,
                NOW);
    }

    private Category createChildCategory() {
        return Category.reconstitute(
                CategoryId.of(5L),
                CategoryCode.of("MENS"),
                CategoryName.of("남성의류"),
                1L,
                CategoryDepth.of(1),
                CategoryPath.of("/1/5/"),
                1,
                false,
                CategoryStatus.ACTIVE,
                NOW,
                NOW);
    }

    private Category createLeafCategory() {
        return Category.reconstitute(
                CategoryId.of(23L),
                CategoryCode.of("MENS_TSHIRTS"),
                CategoryName.of("남성 티셔츠"),
                5L,
                CategoryDepth.of(2),
                CategoryPath.of("/1/5/23/"),
                1,
                true,
                CategoryStatus.ACTIVE,
                NOW,
                NOW);
    }

    private Category createCategoryWithStatus(CategoryStatus status) {
        return Category.reconstitute(
                CategoryId.of(1L),
                CategoryCode.of("FASHION"),
                CategoryName.of("패션"),
                null,
                CategoryDepth.of(0),
                CategoryPath.of("/1/"),
                1,
                false,
                status,
                NOW,
                NOW);
    }
}
