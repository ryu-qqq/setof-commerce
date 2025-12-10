package com.ryuqq.setof.domain.common.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SortDirection")
class SortDirectionTest {

    @Nested
    @DisplayName("defaultDirection")
    class DefaultDirectionTest {

        @Test
        @DisplayName("기본 방향은 DESC")
        void shouldReturnDescAsDefault() {
            // When
            SortDirection result = SortDirection.defaultDirection();

            // Then
            assertEquals(SortDirection.DESC, result);
        }
    }

    @Nested
    @DisplayName("isAscending")
    class IsAscendingTest {

        @Test
        @DisplayName("ASC는 true 반환")
        void shouldReturnTrueForAsc() {
            // Then
            assertTrue(SortDirection.ASC.isAscending());
        }

        @Test
        @DisplayName("DESC는 false 반환")
        void shouldReturnFalseForDesc() {
            // Then
            assertFalse(SortDirection.DESC.isAscending());
        }
    }

    @Nested
    @DisplayName("isDescending")
    class IsDescendingTest {

        @Test
        @DisplayName("DESC는 true 반환")
        void shouldReturnTrueForDesc() {
            // Then
            assertTrue(SortDirection.DESC.isDescending());
        }

        @Test
        @DisplayName("ASC는 false 반환")
        void shouldReturnFalseForAsc() {
            // Then
            assertFalse(SortDirection.ASC.isDescending());
        }
    }

    @Nested
    @DisplayName("reverse")
    class ReverseTest {

        @Test
        @DisplayName("ASC의 reverse는 DESC")
        void shouldReturnDescForAsc() {
            // Then
            assertEquals(SortDirection.DESC, SortDirection.ASC.reverse());
        }

        @Test
        @DisplayName("DESC의 reverse는 ASC")
        void shouldReturnAscForDesc() {
            // Then
            assertEquals(SortDirection.ASC, SortDirection.DESC.reverse());
        }
    }

    @Nested
    @DisplayName("fromString")
    class FromStringTest {

        @Test
        @DisplayName("asc 문자열 파싱")
        void shouldParseAscLowercase() {
            // Then
            assertEquals(SortDirection.ASC, SortDirection.fromString("asc"));
        }

        @Test
        @DisplayName("ASC 대문자 파싱")
        void shouldParseAscUppercase() {
            // Then
            assertEquals(SortDirection.ASC, SortDirection.fromString("ASC"));
        }

        @Test
        @DisplayName("desc 문자열 파싱")
        void shouldParseDescLowercase() {
            // Then
            assertEquals(SortDirection.DESC, SortDirection.fromString("desc"));
        }

        @Test
        @DisplayName("DESC 대문자 파싱")
        void shouldParseDescUppercase() {
            // Then
            assertEquals(SortDirection.DESC, SortDirection.fromString("DESC"));
        }

        @Test
        @DisplayName("null은 기본값 DESC 반환")
        void shouldReturnDefaultForNull() {
            // Then
            assertEquals(SortDirection.DESC, SortDirection.fromString(null));
        }

        @Test
        @DisplayName("빈 문자열은 기본값 DESC 반환")
        void shouldReturnDefaultForEmpty() {
            // Then
            assertEquals(SortDirection.DESC, SortDirection.fromString(""));
        }

        @Test
        @DisplayName("공백 문자열은 기본값 DESC 반환")
        void shouldReturnDefaultForBlank() {
            // Then
            assertEquals(SortDirection.DESC, SortDirection.fromString("   "));
        }

        @Test
        @DisplayName("유효하지 않은 값은 기본값 DESC 반환")
        void shouldReturnDefaultForInvalidValue() {
            // Then
            assertEquals(SortDirection.DESC, SortDirection.fromString("invalid"));
        }

        @Test
        @DisplayName("공백 포함 문자열 트림 후 파싱")
        void shouldTrimAndParse() {
            // Then
            assertEquals(SortDirection.ASC, SortDirection.fromString("  asc  "));
        }
    }

    @Nested
    @DisplayName("enum values")
    class EnumValuesTest {

        @Test
        @DisplayName("2개의 enum 값이 존재")
        void shouldHaveTwoValues() {
            // Then
            assertEquals(2, SortDirection.values().length);
        }
    }

    @Nested
    @DisplayName("displayName")
    class DisplayNameTest {

        @Test
        @DisplayName("ASC는 오름차순 반환")
        void shouldReturnDisplayNameForAsc() {
            assertEquals("오름차순", SortDirection.ASC.displayName());
        }

        @Test
        @DisplayName("DESC는 내림차순 반환")
        void shouldReturnDisplayNameForDesc() {
            assertEquals("내림차순", SortDirection.DESC.displayName());
        }
    }
}
