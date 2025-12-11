package com.ryuqq.setof.application.category.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.category.assembler.CategoryAssembler;
import com.ryuqq.setof.application.category.dto.query.CategorySearchQuery;
import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import com.ryuqq.setof.application.category.factory.query.CategoryQueryFactory;
import com.ryuqq.setof.application.category.manager.query.CategoryReadManager;
import com.ryuqq.setof.domain.category.CategoryFixture;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.query.criteria.CategorySearchCriteria;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("CategoryQueryService")
@ExtendWith(MockitoExtension.class)
class CategoryQueryServiceTest {

    @Mock private CategoryReadManager categoryReadManager;
    @Mock private CategoryQueryFactory categoryQueryFactory;

    private CategoryAssembler categoryAssembler;
    private CategoryQueryService categoryQueryService;

    @BeforeEach
    void setUp() {
        categoryAssembler = new CategoryAssembler();
        categoryQueryService =
                new CategoryQueryService(
                        categoryReadManager, categoryQueryFactory, categoryAssembler);
    }

    @Nested
    @DisplayName("getCategory")
    class GetCategoryTest {

        @Test
        @DisplayName("카테고리 ID로 단일 카테고리 조회 성공")
        void shouldReturnCategoryById() {
            // Given
            Long categoryId = 1L;
            Category category = CategoryFixture.createWithId(categoryId);
            when(categoryReadManager.findById(categoryId)).thenReturn(category);

            // When
            CategoryResponse result = categoryQueryService.getCategory(categoryId);

            // Then
            assertNotNull(result);
            assertEquals(categoryId, result.id());
            verify(categoryReadManager, times(1)).findById(categoryId);
        }

        @Test
        @DisplayName("변환된 응답에 올바른 카테고리 정보 포함")
        void shouldContainCorrectCategoryInfoInResponse() {
            // Given
            Category category = CategoryFixture.createRoot();
            when(categoryReadManager.findById(category.getIdValue())).thenReturn(category);

            // When
            CategoryResponse result = categoryQueryService.getCategory(category.getIdValue());

            // Then
            assertEquals(category.getIdValue(), result.id());
            assertEquals(category.getCodeValue(), result.code());
            assertEquals(category.getNameKoValue(), result.nameKo());
            assertEquals(category.getDepthValue(), result.depth());
        }
    }

    @Nested
    @DisplayName("getCategories")
    class GetCategoriesTest {

        @Test
        @DisplayName("parentId가 null이면 최상위 카테고리 목록 조회")
        void shouldReturnRootCategoriesWhenParentIdIsNull() {
            // Given
            List<Category> categories = CategoryFixture.createList();
            CategorySearchCriteria criteria = CategorySearchCriteria.forRootCategories();

            when(categoryQueryFactory.createCriteria(any(CategorySearchQuery.class)))
                    .thenReturn(criteria);
            when(categoryReadManager.findByCriteria(criteria)).thenReturn(categories);

            // When
            List<CategoryResponse> result = categoryQueryService.getCategories(null);

            // Then
            assertNotNull(result);
            assertEquals(categories.size(), result.size());
            verify(categoryQueryFactory, times(1)).createCriteria(any(CategorySearchQuery.class));
            verify(categoryReadManager, times(1)).findByCriteria(criteria);
        }

        @Test
        @DisplayName("parentId가 주어지면 하위 카테고리 목록 조회")
        void shouldReturnChildCategoriesWhenParentIdIsGiven() {
            // Given
            Long parentId = 1L;
            List<Category> categories = List.of(CategoryFixture.createMiddle());
            when(categoryReadManager.findByParentId(parentId)).thenReturn(categories);

            // When
            List<CategoryResponse> result = categoryQueryService.getCategories(parentId);

            // Then
            assertNotNull(result);
            assertEquals(categories.size(), result.size());
            verify(categoryReadManager, times(1)).findByParentId(parentId);
        }

        @Test
        @DisplayName("하위 카테고리가 없으면 빈 목록 반환")
        void shouldReturnEmptyListWhenNoChildCategories() {
            // Given
            Long parentId = 999L;
            when(categoryReadManager.findByParentId(parentId)).thenReturn(List.of());

            // When
            List<CategoryResponse> result = categoryQueryService.getCategories(parentId);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getCategoryTree")
    class GetCategoryTreeTest {

        @Test
        @DisplayName("전체 카테고리 트리 조회 성공")
        void shouldReturnCategoryTree() {
            // Given
            List<Category> allCategories = CategoryFixture.createHierarchy();
            when(categoryReadManager.findAllActive()).thenReturn(allCategories);

            // When
            List<CategoryTreeResponse> result = categoryQueryService.getCategoryTree();

            // Then
            assertNotNull(result);
            assertFalse(result.isEmpty());
            verify(categoryReadManager, times(1)).findAllActive();
        }

        @Test
        @DisplayName("카테고리가 없으면 빈 트리 반환")
        void shouldReturnEmptyTreeWhenNoCategories() {
            // Given
            when(categoryReadManager.findAllActive()).thenReturn(List.of());

            // When
            List<CategoryTreeResponse> result = categoryQueryService.getCategoryTree();

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getCategoryPath")
    class GetCategoryPathTest {

        @Test
        @DisplayName("카테고리 경로 조회 성공")
        void shouldReturnCategoryPath() {
            // Given
            Long categoryId = 23L;
            Category category = CategoryFixture.createSmall();
            List<Category> pathCategories = CategoryFixture.createHierarchy();

            when(categoryReadManager.findById(categoryId)).thenReturn(category);
            when(categoryReadManager.findByIds(anyList())).thenReturn(pathCategories);

            // When
            CategoryPathResponse result = categoryQueryService.getCategoryPath(categoryId);

            // Then
            assertNotNull(result);
            assertEquals(categoryId, result.categoryId());
            assertNotNull(result.breadcrumbs());
            verify(categoryReadManager, times(1)).findById(categoryId);
            verify(categoryReadManager, times(1)).findByIds(anyList());
        }

        @Test
        @DisplayName("경로에 breadcrumbs 항목이 포함됨")
        void shouldContainBreadcrumbsInPath() {
            // Given
            Long categoryId = 23L;
            Category category = CategoryFixture.createSmall();
            List<Category> pathCategories = CategoryFixture.createHierarchy();

            when(categoryReadManager.findById(categoryId)).thenReturn(category);
            when(categoryReadManager.findByIds(anyList())).thenReturn(pathCategories);

            // When
            CategoryPathResponse result = categoryQueryService.getCategoryPath(categoryId);

            // Then
            assertFalse(result.breadcrumbs().isEmpty());
            assertEquals(3, result.breadcrumbs().size());
        }
    }
}
