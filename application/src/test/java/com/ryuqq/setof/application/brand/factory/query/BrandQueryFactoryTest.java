package com.ryuqq.setof.application.brand.factory.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ryuqq.setof.application.brand.dto.query.BrandSearchQuery;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("BrandQueryFactory")
class BrandQueryFactoryTest {

    private BrandQueryFactory brandQueryFactory;

    @BeforeEach
    void setUp() {
        brandQueryFactory = new BrandQueryFactory();
    }

    @Nested
    @DisplayName("createCriteria")
    class CreateCriteriaTest {

        @Test
        @DisplayName("BrandSearchQuery를 BrandSearchCriteria로 변환 성공")
        void shouldConvertQueryToCriteria() {
            // Given
            BrandSearchQuery query = BrandSearchQuery.of("Nike", "ACTIVE", 0, 20);

            // When
            BrandSearchCriteria criteria = brandQueryFactory.createCriteria(query);

            // Then
            assertNotNull(criteria);
            assertEquals("Nike", criteria.keyword());
            assertEquals("ACTIVE", criteria.status());
            assertEquals(0, criteria.page());
            assertEquals(20, criteria.size());
        }

        @Test
        @DisplayName("키워드 검색 조건 변환 성공")
        void shouldConvertKeywordQuery() {
            // Given
            BrandSearchQuery query = BrandSearchQuery.ofKeyword("Adidas", 1, 10);

            // When
            BrandSearchCriteria criteria = brandQueryFactory.createCriteria(query);

            // Then
            assertNotNull(criteria);
            assertEquals("Adidas", criteria.keyword());
            assertNull(criteria.status());
            assertEquals(1, criteria.page());
            assertEquals(10, criteria.size());
        }

        @Test
        @DisplayName("활성 브랜드 검색 조건 변환 성공")
        void shouldConvertActiveQuery() {
            // Given
            BrandSearchQuery query = BrandSearchQuery.ofActive(2, 15);

            // When
            BrandSearchCriteria criteria = brandQueryFactory.createCriteria(query);

            // Then
            assertNotNull(criteria);
            assertNull(criteria.keyword());
            assertEquals("ACTIVE", criteria.status());
            assertEquals(2, criteria.page());
            assertEquals(15, criteria.size());
        }

        @Test
        @DisplayName("null 키워드 처리")
        void shouldHandleNullKeyword() {
            // Given
            BrandSearchQuery query = BrandSearchQuery.of(null, "ACTIVE", 0, 20);

            // When
            BrandSearchCriteria criteria = brandQueryFactory.createCriteria(query);

            // Then
            assertNotNull(criteria);
            assertNull(criteria.keyword());
            assertEquals("ACTIVE", criteria.status());
        }

        @Test
        @DisplayName("null 상태 처리")
        void shouldHandleNullStatus() {
            // Given
            BrandSearchQuery query = BrandSearchQuery.of("Nike", null, 0, 20);

            // When
            BrandSearchCriteria criteria = brandQueryFactory.createCriteria(query);

            // Then
            assertNotNull(criteria);
            assertEquals("Nike", criteria.keyword());
            assertNull(criteria.status());
        }
    }
}
