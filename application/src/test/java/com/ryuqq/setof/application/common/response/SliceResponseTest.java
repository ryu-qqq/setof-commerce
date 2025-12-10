package com.ryuqq.setof.application.common.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SliceResponse")
class SliceResponseTest {

    @Nested
    @DisplayName("of (with cursor)")
    class OfWithCursorTest {

        @Test
        @DisplayName("커서와 함께 SliceResponse 생성 성공")
        void shouldCreateSliceResponseWithCursor() {
            // Given
            List<String> content = List.of("item1", "item2", "item3");
            int size = 10;
            boolean hasNext = true;
            String nextCursor = "cursor-123";

            // When
            SliceResponse<String> result = SliceResponse.of(content, size, hasNext, nextCursor);

            // Then
            assertNotNull(result);
            assertEquals(content, result.content());
            assertEquals(size, result.size());
            assertTrue(result.hasNext());
            assertEquals(nextCursor, result.nextCursor());
        }

        @Test
        @DisplayName("다음 페이지가 없는 경우")
        void shouldCreateSliceResponseWithNoNext() {
            // Given
            List<String> content = List.of("item1", "item2");
            int size = 10;
            boolean hasNext = false;
            String nextCursor = null;

            // When
            SliceResponse<String> result = SliceResponse.of(content, size, hasNext, nextCursor);

            // Then
            assertFalse(result.hasNext());
            assertNull(result.nextCursor());
        }
    }

    @Nested
    @DisplayName("of (without cursor)")
    class OfWithoutCursorTest {

        @Test
        @DisplayName("커서 없이 SliceResponse 생성 성공")
        void shouldCreateSliceResponseWithoutCursor() {
            // Given
            List<String> content = List.of("item1", "item2", "item3");
            int size = 10;
            boolean hasNext = true;

            // When
            SliceResponse<String> result = SliceResponse.of(content, size, hasNext);

            // Then
            assertNotNull(result);
            assertEquals(content, result.content());
            assertEquals(size, result.size());
            assertTrue(result.hasNext());
            assertNull(result.nextCursor());
        }
    }

    @Nested
    @DisplayName("empty")
    class EmptyTest {

        @Test
        @DisplayName("빈 SliceResponse 생성 성공")
        void shouldCreateEmptySliceResponse() {
            // Given
            int size = 20;

            // When
            SliceResponse<String> result = SliceResponse.empty(size);

            // Then
            assertNotNull(result);
            assertTrue(result.content().isEmpty());
            assertEquals(size, result.size());
            assertFalse(result.hasNext());
            assertNull(result.nextCursor());
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
            int size = 5;
            boolean hasNext = true;
            String nextCursor = "next-page";

            // When
            SliceResponse<Integer> result = new SliceResponse<>(content, size, hasNext, nextCursor);

            // Then
            assertEquals(5, result.content().size());
            assertEquals(1, result.content().get(0));
            assertEquals(5, result.content().get(4));
        }
    }
}
