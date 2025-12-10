package com.ryuqq.setof.domain.common.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CursorPageRequest")
class CursorPageRequestTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("정상 값으로 생성")
        void shouldCreateWithValidValues() {
            // When
            CursorPageRequest request = CursorPageRequest.of("cursor-123", 30);

            // Then
            assertEquals("cursor-123", request.cursor());
            assertEquals(30, request.size());
        }

        @Test
        @DisplayName("0 이하 size는 기본값으로 정규화")
        void shouldNormalizeZeroSize() {
            // When
            CursorPageRequest request = CursorPageRequest.of("cursor", 0);

            // Then
            assertEquals(CursorPageRequest.DEFAULT_SIZE, request.size());
        }

        @Test
        @DisplayName("음수 size는 기본값으로 정규화")
        void shouldNormalizeNegativeSize() {
            // When
            CursorPageRequest request = CursorPageRequest.of("cursor", -10);

            // Then
            assertEquals(CursorPageRequest.DEFAULT_SIZE, request.size());
        }

        @Test
        @DisplayName("MAX_SIZE 초과 size는 MAX_SIZE로 정규화")
        void shouldNormalizeSizeExceedingMax() {
            // When
            CursorPageRequest request = CursorPageRequest.of("cursor", 200);

            // Then
            assertEquals(CursorPageRequest.MAX_SIZE, request.size());
        }

        @Test
        @DisplayName("빈 문자열 cursor는 null로 정규화")
        void shouldNormalizeEmptyCursorToNull() {
            // When
            CursorPageRequest request = CursorPageRequest.of("", 20);

            // Then
            assertNull(request.cursor());
        }

        @Test
        @DisplayName("공백 문자열 cursor는 null로 정규화")
        void shouldNormalizeBlankCursorToNull() {
            // When
            CursorPageRequest request = CursorPageRequest.of("   ", 20);

            // Then
            assertNull(request.cursor());
        }
    }

    @Nested
    @DisplayName("팩토리 메서드 테스트")
    class FactoryMethodTest {

        @Test
        @DisplayName("first는 cursor=null로 생성")
        void shouldCreateFirstPage() {
            // When
            CursorPageRequest request = CursorPageRequest.first(25);

            // Then
            assertNull(request.cursor());
            assertEquals(25, request.size());
        }

        @Test
        @DisplayName("defaultPage는 cursor=null, size=DEFAULT_SIZE로 생성")
        void shouldCreateDefaultPage() {
            // When
            CursorPageRequest request = CursorPageRequest.defaultPage();

            // Then
            assertNull(request.cursor());
            assertEquals(CursorPageRequest.DEFAULT_SIZE, request.size());
        }

        @Test
        @DisplayName("afterId는 Long을 문자열 cursor로 변환")
        void shouldCreateAfterId() {
            // When
            CursorPageRequest request = CursorPageRequest.afterId(12345L, 20);

            // Then
            assertEquals("12345", request.cursor());
            assertEquals(20, request.size());
        }

        @Test
        @DisplayName("afterId에 null ID는 cursor=null")
        void shouldCreateAfterIdWithNullId() {
            // When
            CursorPageRequest request = CursorPageRequest.afterId(null, 20);

            // Then
            assertNull(request.cursor());
        }
    }

    @Nested
    @DisplayName("isFirstPage 테스트")
    class IsFirstPageTest {

        @Test
        @DisplayName("cursor가 null이면 true")
        void shouldReturnTrueForNullCursor() {
            // When
            CursorPageRequest request = CursorPageRequest.first(20);

            // Then
            assertTrue(request.isFirstPage());
        }

        @Test
        @DisplayName("cursor가 있으면 false")
        void shouldReturnFalseForNonNullCursor() {
            // When
            CursorPageRequest request = CursorPageRequest.of("cursor", 20);

            // Then
            assertFalse(request.isFirstPage());
        }
    }

    @Nested
    @DisplayName("hasCursor 테스트")
    class HasCursorTest {

        @Test
        @DisplayName("cursor가 있으면 true")
        void shouldReturnTrueForNonNullCursor() {
            // When
            CursorPageRequest request = CursorPageRequest.of("cursor", 20);

            // Then
            assertTrue(request.hasCursor());
        }

        @Test
        @DisplayName("cursor가 null이면 false")
        void shouldReturnFalseForNullCursor() {
            // When
            CursorPageRequest request = CursorPageRequest.first(20);

            // Then
            assertFalse(request.hasCursor());
        }
    }

    @Nested
    @DisplayName("cursorAsLong 테스트")
    class CursorAsLongTest {

        @Test
        @DisplayName("숫자 문자열 cursor를 Long으로 변환")
        void shouldParseCursorAsLong() {
            // Given
            CursorPageRequest request = CursorPageRequest.of("12345", 20);

            // Then
            assertEquals(12345L, request.cursorAsLong());
        }

        @Test
        @DisplayName("null cursor는 null 반환")
        void shouldReturnNullForNullCursor() {
            // Given
            CursorPageRequest request = CursorPageRequest.first(20);

            // Then
            assertNull(request.cursorAsLong());
        }

        @Test
        @DisplayName("숫자가 아닌 cursor는 null 반환")
        void shouldReturnNullForNonNumericCursor() {
            // Given
            CursorPageRequest request = CursorPageRequest.of("not-a-number", 20);

            // Then
            assertNull(request.cursorAsLong());
        }
    }

    @Nested
    @DisplayName("next 테스트")
    class NextTest {

        @Test
        @DisplayName("다음 페이지 요청 생성")
        void shouldCreateNextPage() {
            // Given
            CursorPageRequest request = CursorPageRequest.of("cursor-1", 20);

            // When
            CursorPageRequest next = request.next("cursor-2");

            // Then
            assertEquals("cursor-2", next.cursor());
            assertEquals(20, next.size());
        }
    }

    @Nested
    @DisplayName("fetchSize 테스트")
    class FetchSizeTest {

        @Test
        @DisplayName("size + 1 반환")
        void shouldReturnSizePlusOne() {
            // Given
            CursorPageRequest request = CursorPageRequest.of("cursor", 20);

            // Then
            assertEquals(21, request.fetchSize());
        }
    }

    @Nested
    @DisplayName("상수 테스트")
    class ConstantsTest {

        @Test
        @DisplayName("DEFAULT_SIZE는 20")
        void shouldHaveDefaultSize20() {
            assertEquals(20, CursorPageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("MAX_SIZE는 100")
        void shouldHaveMaxSize100() {
            assertEquals(100, CursorPageRequest.MAX_SIZE);
        }
    }
}
