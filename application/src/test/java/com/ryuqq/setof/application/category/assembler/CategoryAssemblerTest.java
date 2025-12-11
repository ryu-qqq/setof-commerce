package com.ryuqq.setof.application.category.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import com.ryuqq.setof.domain.category.CategoryFixture;
import com.ryuqq.setof.domain.category.aggregate.Category;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CategoryAssembler")
class CategoryAssemblerTest {

    private CategoryAssembler categoryAssembler;

    @BeforeEach
    void setUp() {
        categoryAssembler = new CategoryAssembler();
    }

    @Nested
    @DisplayName("toCategoryResponse")
    class ToCategoryResponseTest {

        @Test
        @DisplayName("Category 도메인을 CategoryResponse로 변환 성공")
        void shouldConvertCategoryToCategoryResponse() {
            // Given
            Category category = CategoryFixture.createRoot();

            // When
            CategoryResponse result = categoryAssembler.toCategoryResponse(category);

            // Then
            assertNotNull(result);
            assertEquals(category.getIdValue(), result.id());
            assertEquals(category.getCodeValue(), result.code());
            assertEquals(category.getNameKoValue(), result.nameKo());
            assertEquals(category.getDepthValue(), result.depth());
            assertEquals(category.getPathValue(), result.path());
        }

        @Test
        @DisplayName("중분류 카테고리 변환 성공")
        void shouldConvertMiddleCategoryToResponse() {
            // Given
            Category category = CategoryFixture.createMiddle();

            // When
            CategoryResponse result = categoryAssembler.toCategoryResponse(category);

            // Then
            assertNotNull(result);
            assertEquals(1, result.depth());
            assertEquals(1L, result.parentId());
        }

        @Test
        @DisplayName("소분류(리프) 카테고리 변환 성공")
        void shouldConvertLeafCategoryToResponse() {
            // Given
            Category category = CategoryFixture.createSmall();

            // When
            CategoryResponse result = categoryAssembler.toCategoryResponse(category);

            // Then
            assertNotNull(result);
            assertEquals(2, result.depth());
            assertTrue(result.isLeaf());
        }
    }

    @Nested
    @DisplayName("toCategoryResponses")
    class ToCategoryResponsesTest {

        @Test
        @DisplayName("Category 목록을 CategoryResponse 목록으로 변환 성공")
        void shouldConvertCategoryListToCategoryResponseList() {
            // Given
            List<Category> categories = CategoryFixture.createList();

            // When
            List<CategoryResponse> results = categoryAssembler.toCategoryResponses(categories);

            // Then
            assertNotNull(results);
            assertEquals(categories.size(), results.size());

            for (int i = 0; i < categories.size(); i++) {
                assertEquals(categories.get(i).getIdValue(), results.get(i).id());
            }
        }

        @Test
        @DisplayName("빈 목록 변환 시 빈 목록 반환")
        void shouldReturnEmptyListWhenInputIsEmpty() {
            // Given
            List<Category> emptyList = List.of();

            // When
            List<CategoryResponse> results = categoryAssembler.toCategoryResponses(emptyList);

            // Then
            assertNotNull(results);
            assertTrue(results.isEmpty());
        }
    }

    @Nested
    @DisplayName("toCategoryTreeResponses")
    class ToCategoryTreeResponsesTest {

        @Test
        @DisplayName("계층 구조를 가진 카테고리 목록을 트리 구조로 변환 성공")
        void shouldConvertCategoryListToTreeStructure() {
            // Given
            List<Category> hierarchyCategories = CategoryFixture.createHierarchy();

            // When
            List<CategoryTreeResponse> results =
                    categoryAssembler.toCategoryTreeResponses(hierarchyCategories);

            // Then
            assertNotNull(results);
            assertFalse(results.isEmpty());

            // 최상위 카테고리 확인
            CategoryTreeResponse rootCategory = results.get(0);
            assertEquals("패션", rootCategory.nameKo());
            assertEquals(0, rootCategory.depth());

            // 하위 카테고리 확인
            assertFalse(rootCategory.children().isEmpty());
        }

        @Test
        @DisplayName("빈 목록은 빈 트리 반환")
        void shouldReturnEmptyTreeWhenNoCategories() {
            // Given
            List<Category> emptyList = List.of();

            // When
            List<CategoryTreeResponse> results =
                    categoryAssembler.toCategoryTreeResponses(emptyList);

            // Then
            assertNotNull(results);
            assertTrue(results.isEmpty());
        }
    }

    @Nested
    @DisplayName("toCategoryPathResponse")
    class ToCategoryPathResponseTest {

        @Test
        @DisplayName("카테고리 경로를 CategoryPathResponse로 변환 성공")
        void shouldConvertCategoryPathToResponse() {
            // Given
            Long categoryId = 23L;
            List<Category> pathCategories = CategoryFixture.createHierarchy();

            // When
            CategoryPathResponse result =
                    categoryAssembler.toCategoryPathResponse(categoryId, pathCategories);

            // Then
            assertNotNull(result);
            assertEquals(categoryId, result.categoryId());
            assertNotNull(result.breadcrumbs());
            assertEquals(3, result.breadcrumbs().size());
        }

        @Test
        @DisplayName("경로 breadcrumbs는 깊이순으로 정렬")
        void shouldSortBreadcrumbsByDepth() {
            // Given
            Long categoryId = 23L;
            List<Category> pathCategories = CategoryFixture.createHierarchy();

            // When
            CategoryPathResponse result =
                    categoryAssembler.toCategoryPathResponse(categoryId, pathCategories);

            // Then
            var breadcrumbs = result.breadcrumbs();
            assertEquals(0, breadcrumbs.get(0).depth());
            assertEquals(1, breadcrumbs.get(1).depth());
            assertEquals(2, breadcrumbs.get(2).depth());
        }
    }
}
