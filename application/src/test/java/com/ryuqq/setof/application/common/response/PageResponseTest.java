package com.ryuqq.setof.application.common.response;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PageResponse")
class PageResponseTest {

    @Nested
    @DisplayName("of")
    class OfTest {

        @Test
        @DisplayName("PageResponse 생성 성공")
        void shouldCreatePageResponse() {
            // Given
            List<String> content = List.of("item1", "item2", "item3");
            int page = 0;
            int size = 10;
            long totalElements = 100L;
            int totalPages = 10;
            boolean first = true;
            boolean last = false;

            // When
            PageResponse<String> result =
                    PageResponse.of(content, page, size, totalElements, totalPages, first, last);

            // Then
            assertNotNull(result);
            assertEquals(content, result.content());
            assertEquals(page, result.page());
            assertEquals(size, result.size());
            assertEquals(totalElements, result.totalElements());
            assertEquals(totalPages, result.totalPages());
            assertTrue(result.first());
            assertFalse(result.last());
        }

        @Test
        @DisplayName("마지막 페이지 PageResponse 생성")
        void shouldCreateLastPageResponse() {
            // Given
            List<String> content = List.of("item91", "item92");
            int page = 9;
            int size = 10;
            long totalElements = 92L;
            int totalPages = 10;
            boolean first = false;
            boolean last = true;

            // When
            PageResponse<String> result =
                    PageResponse.of(content, page, size, totalElements, totalPages, first, last);

            // Then
            assertFalse(result.first());
            assertTrue(result.last());
            assertEquals(9, result.page());
        }

        @Test
        @DisplayName("중간 페이지 PageResponse 생성")
        void shouldCreateMiddlePageResponse() {
            // Given
            List<String> content = List.of("item1", "item2");
            int page = 5;
            int size = 10;
            long totalElements = 100L;
            int totalPages = 10;
            boolean first = false;
            boolean last = false;

            // When
            PageResponse<String> result =
                    PageResponse.of(content, page, size, totalElements, totalPages, first, last);

            // Then
            assertFalse(result.first());
            assertFalse(result.last());
            assertEquals(5, result.page());
        }
    }

    @Nested
    @DisplayName("empty")
    class EmptyTest {

        @Test
        @DisplayName("빈 PageResponse 생성 성공")
        void shouldCreateEmptyPageResponse() {
            // Given
            int page = 0;
            int size = 20;

            // When
            PageResponse<String> result = PageResponse.empty(page, size);

            // Then
            assertNotNull(result);
            assertTrue(result.content().isEmpty());
            assertEquals(page, result.page());
            assertEquals(size, result.size());
            assertEquals(0L, result.totalElements());
            assertEquals(0, result.totalPages());
            assertTrue(result.first());
            assertTrue(result.last());
        }

        @Test
        @DisplayName("다른 페이지 번호로 빈 PageResponse 생성")
        void shouldCreateEmptyPageResponseWithDifferentPage() {
            // Given
            int page = 5;
            int size = 10;

            // When
            PageResponse<String> result = PageResponse.empty(page, size);

            // Then
            assertEquals(page, result.page());
            assertEquals(size, result.size());
        }
    }

    @Nested
    @DisplayName("record accessors")
    class RecordAccessorsTest {

        @Test
        @DisplayName("record 컴포넌트 접근자 동작 확인")
        void shouldAccessRecordComponents() {
            // Given
            List<Integer> content = List.of(1, 2, 3, 4, 5);
            int page = 0;
            int size = 5;
            long totalElements = 25L;
            int totalPages = 5;
            boolean first = true;
            boolean last = false;

            // When
            PageResponse<Integer> result =
                    new PageResponse<>(content, page, size, totalElements, totalPages, first, last);

            // Then
            assertEquals(5, result.content().size());
            assertEquals(1, result.content().get(0));
            assertEquals(5, result.content().get(4));
            assertEquals(25L, result.totalElements());
            assertEquals(5, result.totalPages());
        }
    }
}
