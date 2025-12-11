package com.ryuqq.setof.application.category.factory.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.category.dto.query.CategorySearchQuery;
import com.ryuqq.setof.domain.category.query.criteria.CategorySearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CategoryQueryFactory")
class CategoryQueryFactoryTest {

    private CategoryQueryFactory categoryQueryFactory;

    @BeforeEach
    void setUp() {
        categoryQueryFactory = new CategoryQueryFactory();
    }

    @Nested
    @DisplayName("createCriteria")
    class CreateCriteriaTest {

        @Test
        @DisplayName("최상위 카테고리 검색 조건 변환 성공")
        void shouldConvertRootCategoriesQuery() {
            // Given
            CategorySearchQuery query = CategorySearchQuery.forRootCategories();

            // When
            CategorySearchCriteria criteria = categoryQueryFactory.createCriteria(query);

            // Then
            assertNotNull(criteria);
            assertNull(criteria.parentId());
            assertEquals(0, criteria.depth());
            assertEquals("ACTIVE", criteria.status());
            assertFalse(criteria.includeInactive());
        }

        @Test
        @DisplayName("하위 카테고리 검색 조건 변환 성공")
        void shouldConvertChildCategoriesQuery() {
            // Given
            Long parentId = 1L;
            CategorySearchQuery query = CategorySearchQuery.forChildren(parentId);

            // When
            CategorySearchCriteria criteria = categoryQueryFactory.createCriteria(query);

            // Then
            assertNotNull(criteria);
            assertEquals(parentId, criteria.parentId());
            assertNull(criteria.depth());
            assertEquals("ACTIVE", criteria.status());
            assertFalse(criteria.includeInactive());
        }

        @Test
        @DisplayName("모든 필드가 설정된 쿼리 변환 성공")
        void shouldConvertFullQuery() {
            // Given
            CategorySearchQuery query = new CategorySearchQuery(10L, 2, "INACTIVE", true);

            // When
            CategorySearchCriteria criteria = categoryQueryFactory.createCriteria(query);

            // Then
            assertNotNull(criteria);
            assertEquals(10L, criteria.parentId());
            assertEquals(2, criteria.depth());
            assertEquals("INACTIVE", criteria.status());
            assertTrue(criteria.includeInactive());
        }

        @Test
        @DisplayName("null parentId 처리")
        void shouldHandleNullParentId() {
            // Given
            CategorySearchQuery query = new CategorySearchQuery(null, 0, "ACTIVE", false);

            // When
            CategorySearchCriteria criteria = categoryQueryFactory.createCriteria(query);

            // Then
            assertNotNull(criteria);
            assertNull(criteria.parentId());
            assertEquals(0, criteria.depth());
        }

        @Test
        @DisplayName("null depth 처리")
        void shouldHandleNullDepth() {
            // Given
            CategorySearchQuery query = new CategorySearchQuery(1L, null, "ACTIVE", false);

            // When
            CategorySearchCriteria criteria = categoryQueryFactory.createCriteria(query);

            // Then
            assertNotNull(criteria);
            assertEquals(1L, criteria.parentId());
            assertNull(criteria.depth());
        }
    }
}
