package com.ryuqq.setof.application.category.manager.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.category.port.out.query.CategoryQueryPort;
import com.ryuqq.setof.domain.category.CategoryFixture;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.exception.CategoryNotFoundException;
import com.ryuqq.setof.domain.category.query.criteria.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.vo.CategoryCode;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("CategoryReadManager")
@ExtendWith(MockitoExtension.class)
class CategoryReadManagerTest {

    @Mock private CategoryQueryPort categoryQueryPort;

    private CategoryReadManager categoryReadManager;

    @BeforeEach
    void setUp() {
        categoryReadManager = new CategoryReadManager(categoryQueryPort);
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 카테고리 ID로 조회 성공")
        void shouldFindCategoryById() {
            // Given
            Long categoryId = 1L;
            Category category = CategoryFixture.createWithId(categoryId);
            when(categoryQueryPort.findById(any(CategoryId.class)))
                    .thenReturn(Optional.of(category));

            // When
            Category result = categoryReadManager.findById(categoryId);

            // Then
            assertNotNull(result);
            assertEquals(categoryId, result.getIdValue());
            verify(categoryQueryPort, times(1)).findById(any(CategoryId.class));
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 ID로 조회 시 예외 발생")
        void shouldThrowExceptionWhenCategoryNotFound() {
            // Given
            Long categoryId = 999L;
            when(categoryQueryPort.findById(any(CategoryId.class))).thenReturn(Optional.empty());

            // When & Then
            assertThrows(
                    CategoryNotFoundException.class,
                    () -> categoryReadManager.findById(categoryId));
            verify(categoryQueryPort, times(1)).findById(any(CategoryId.class));
        }
    }

    @Nested
    @DisplayName("findByIdOptional")
    class FindByIdOptionalTest {

        @Test
        @DisplayName("존재하는 카테고리 ID로 조회 성공")
        void shouldFindCategoryByIdOptional() {
            // Given
            Long categoryId = 1L;
            Category category = CategoryFixture.createWithId(categoryId);
            when(categoryQueryPort.findById(any(CategoryId.class)))
                    .thenReturn(Optional.of(category));

            // When
            Optional<Category> result = categoryReadManager.findByIdOptional(categoryId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(categoryId, result.get().getIdValue());
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 ID로 조회 시 빈 Optional 반환")
        void shouldReturnEmptyOptionalWhenCategoryNotFound() {
            // Given
            Long categoryId = 999L;
            when(categoryQueryPort.findById(any(CategoryId.class))).thenReturn(Optional.empty());

            // When
            Optional<Category> result = categoryReadManager.findByIdOptional(categoryId);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findByCode")
    class FindByCodeTest {

        @Test
        @DisplayName("존재하는 카테고리 코드로 조회 성공")
        void shouldFindCategoryByCode() {
            // Given
            String categoryCode = "FASHION";
            Category category = CategoryFixture.createRoot();
            when(categoryQueryPort.findByCode(any(CategoryCode.class)))
                    .thenReturn(Optional.of(category));

            // When
            Category result = categoryReadManager.findByCode(categoryCode);

            // Then
            assertNotNull(result);
            assertEquals(categoryCode, result.getCodeValue());
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 코드로 조회 시 예외 발생")
        void shouldThrowExceptionWhenCategoryCodeNotFound() {
            // Given
            String categoryCode = "UNKNOWN";
            when(categoryQueryPort.findByCode(any(CategoryCode.class)))
                    .thenReturn(Optional.empty());

            // When & Then
            assertThrows(
                    CategoryNotFoundException.class,
                    () -> categoryReadManager.findByCode(categoryCode));
        }
    }

    @Nested
    @DisplayName("findByParentId")
    class FindByParentIdTest {

        @Test
        @DisplayName("부모 ID로 하위 카테고리 목록 조회 성공")
        void shouldFindCategoriesByParentId() {
            // Given
            Long parentId = 1L;
            List<Category> categories = List.of(CategoryFixture.createMiddle());
            when(categoryQueryPort.findByParentId(any(CategoryId.class))).thenReturn(categories);

            // When
            List<Category> result = categoryReadManager.findByParentId(parentId);

            // Then
            assertNotNull(result);
            assertEquals(categories.size(), result.size());
            verify(categoryQueryPort, times(1)).findByParentId(any(CategoryId.class));
        }
    }

    @Nested
    @DisplayName("findAllActive")
    class FindAllActiveTest {

        @Test
        @DisplayName("활성 카테고리 목록 조회 성공")
        void shouldFindAllActiveCategories() {
            // Given
            List<Category> categories = CategoryFixture.createList();
            when(categoryQueryPort.findAllActive()).thenReturn(categories);

            // When
            List<Category> result = categoryReadManager.findAllActive();

            // Then
            assertNotNull(result);
            assertEquals(categories.size(), result.size());
            verify(categoryQueryPort, times(1)).findAllActive();
        }
    }

    @Nested
    @DisplayName("findByCriteria")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 카테고리 목록 조회 성공")
        void shouldFindCategoriesByCriteria() {
            // Given
            CategorySearchCriteria criteria = CategorySearchCriteria.forRootCategories();
            List<Category> categories = CategoryFixture.createList();
            when(categoryQueryPort.findByCriteria(criteria)).thenReturn(categories);

            // When
            List<Category> result = categoryReadManager.findByCriteria(criteria);

            // Then
            assertNotNull(result);
            assertEquals(categories.size(), result.size());
            verify(categoryQueryPort, times(1)).findByCriteria(criteria);
        }
    }

    @Nested
    @DisplayName("findByIds")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 카테고리 목록 조회 성공")
        void shouldFindCategoriesByIds() {
            // Given
            List<Long> categoryIds = List.of(1L, 2L, 3L);
            List<Category> categories = CategoryFixture.createList();
            when(categoryQueryPort.findByIds(categoryIds)).thenReturn(categories);

            // When
            List<Category> result = categoryReadManager.findByIds(categoryIds);

            // Then
            assertNotNull(result);
            assertEquals(categories.size(), result.size());
            verify(categoryQueryPort, times(1)).findByIds(categoryIds);
        }
    }

    @Nested
    @DisplayName("existsById")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 카테고리 ID는 true 반환")
        void shouldReturnTrueWhenCategoryExists() {
            // Given
            Long categoryId = 1L;
            when(categoryQueryPort.existsById(any(CategoryId.class))).thenReturn(true);

            // When
            boolean result = categoryReadManager.existsById(categoryId);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 ID는 false 반환")
        void shouldReturnFalseWhenCategoryNotExists() {
            // Given
            Long categoryId = 999L;
            when(categoryQueryPort.existsById(any(CategoryId.class))).thenReturn(false);

            // When
            boolean result = categoryReadManager.existsById(categoryId);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("existsActiveById")
    class ExistsActiveByIdTest {

        @Test
        @DisplayName("활성 카테고리가 존재하면 true 반환")
        void shouldReturnTrueWhenActiveCategoryExists() {
            // Given
            Long categoryId = 1L;
            when(categoryQueryPort.existsActiveById(categoryId)).thenReturn(true);

            // When
            boolean result = categoryReadManager.existsActiveById(categoryId);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("활성 카테고리가 존재하지 않으면 false 반환")
        void shouldReturnFalseWhenActiveCategoryNotExists() {
            // Given
            Long categoryId = 999L;
            when(categoryQueryPort.existsActiveById(categoryId)).thenReturn(false);

            // When
            boolean result = categoryReadManager.existsActiveById(categoryId);

            // Then
            assertFalse(result);
        }
    }
}
