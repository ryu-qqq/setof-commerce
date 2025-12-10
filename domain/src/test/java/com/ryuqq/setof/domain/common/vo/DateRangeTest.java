package com.ryuqq.setof.domain.common.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DateRange")
class DateRangeTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("정상 범위로 생성")
        void shouldCreateWithValidRange() {
            // Given
            LocalDate start = LocalDate.of(2025, 1, 1);
            LocalDate end = LocalDate.of(2025, 1, 31);

            // When
            DateRange range = DateRange.of(start, end);

            // Then
            assertEquals(start, range.startDate());
            assertEquals(end, range.endDate());
        }

        @Test
        @DisplayName("같은 날짜로 생성 가능")
        void shouldCreateWithSameDate() {
            // Given
            LocalDate date = LocalDate.of(2025, 1, 15);

            // When
            DateRange range = DateRange.of(date, date);

            // Then
            assertEquals(date, range.startDate());
            assertEquals(date, range.endDate());
        }

        @Test
        @DisplayName("시작일이 종료일보다 늦으면 예외")
        void shouldThrowExceptionWhenStartAfterEnd() {
            // Given
            LocalDate start = LocalDate.of(2025, 2, 1);
            LocalDate end = LocalDate.of(2025, 1, 1);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> DateRange.of(start, end));
        }

        @Test
        @DisplayName("null 시작일 허용")
        void shouldAllowNullStartDate() {
            // Given
            LocalDate end = LocalDate.of(2025, 1, 31);

            // When
            DateRange range = DateRange.of(null, end);

            // Then
            assertNull(range.startDate());
            assertEquals(end, range.endDate());
        }

        @Test
        @DisplayName("null 종료일 허용")
        void shouldAllowNullEndDate() {
            // Given
            LocalDate start = LocalDate.of(2025, 1, 1);

            // When
            DateRange range = DateRange.of(start, null);

            // Then
            assertEquals(start, range.startDate());
            assertNull(range.endDate());
        }
    }

    @Nested
    @DisplayName("팩토리 메서드 테스트")
    class FactoryMethodTest {

        @Test
        @DisplayName("lastDays 생성")
        void shouldCreateLastDays() {
            // When
            DateRange range = DateRange.lastDays(7);

            // Then
            assertNotNull(range.startDate());
            assertNotNull(range.endDate());
            assertEquals(LocalDate.now(), range.endDate());
            assertEquals(LocalDate.now().minusDays(7), range.startDate());
        }

        @Test
        @DisplayName("lastDays 0은 오늘만")
        void shouldCreateLastDaysZero() {
            // When
            DateRange range = DateRange.lastDays(0);

            // Then
            assertEquals(LocalDate.now(), range.startDate());
            assertEquals(LocalDate.now(), range.endDate());
        }

        @Test
        @DisplayName("lastDays 음수면 예외")
        void shouldThrowExceptionForNegativeDays() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> DateRange.lastDays(-1));
        }

        @Test
        @DisplayName("thisMonth 생성")
        void shouldCreateThisMonth() {
            // When
            DateRange range = DateRange.thisMonth();

            // Then
            LocalDate today = LocalDate.now();
            assertEquals(today.withDayOfMonth(1), range.startDate());
            assertEquals(today.withDayOfMonth(today.lengthOfMonth()), range.endDate());
        }

        @Test
        @DisplayName("lastMonth 생성")
        void shouldCreateLastMonth() {
            // When
            DateRange range = DateRange.lastMonth();

            // Then
            LocalDate today = LocalDate.now();
            LocalDate expectedStart = today.minusMonths(1).withDayOfMonth(1);
            LocalDate expectedEnd = today.withDayOfMonth(1).minusDays(1);
            assertEquals(expectedStart, range.startDate());
            assertEquals(expectedEnd, range.endDate());
        }

        @Test
        @DisplayName("until 생성")
        void shouldCreateUntil() {
            // Given
            LocalDate end = LocalDate.of(2025, 6, 30);

            // When
            DateRange range = DateRange.until(end);

            // Then
            assertNull(range.startDate());
            assertEquals(end, range.endDate());
        }

        @Test
        @DisplayName("from 생성")
        void shouldCreateFrom() {
            // Given
            LocalDate start = LocalDate.of(2025, 1, 1);

            // When
            DateRange range = DateRange.from(start);

            // Then
            assertEquals(start, range.startDate());
            assertNull(range.endDate());
        }
    }

    @Nested
    @DisplayName("Instant 변환 테스트")
    class InstantConversionTest {

        @Test
        @DisplayName("startInstant 변환")
        void shouldConvertStartInstant() {
            // Given
            LocalDate start = LocalDate.of(2025, 1, 15);
            DateRange range = DateRange.of(start, LocalDate.of(2025, 1, 31));

            // When
            Instant instant = range.startInstant();

            // Then
            assertNotNull(instant);
            Instant expected = start.atStartOfDay(ZoneId.systemDefault()).toInstant();
            assertEquals(expected, instant);
        }

        @Test
        @DisplayName("null startDate는 null Instant")
        void shouldReturnNullInstantForNullStartDate() {
            // Given
            DateRange range = DateRange.until(LocalDate.of(2025, 1, 31));

            // Then
            assertNull(range.startInstant());
        }

        @Test
        @DisplayName("endInstant 변환 (23:59:59.999999999)")
        void shouldConvertEndInstant() {
            // Given
            LocalDate end = LocalDate.of(2025, 1, 15);
            DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), end);

            // When
            Instant instant = range.endInstant();

            // Then
            assertNotNull(instant);
            // endDate의 다음날 00:00:00에서 1나노초 뺀 값
            Instant expected =
                    end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().minusNanos(1);
            assertEquals(expected, instant);
        }

        @Test
        @DisplayName("null endDate는 null Instant")
        void shouldReturnNullInstantForNullEndDate() {
            // Given
            DateRange range = DateRange.from(LocalDate.of(2025, 1, 1));

            // Then
            assertNull(range.endInstant());
        }
    }

    @Nested
    @DisplayName("isEmpty 테스트")
    class IsEmptyTest {

        @Test
        @DisplayName("둘 다 null이면 true")
        void shouldReturnTrueWhenBothNull() {
            // When
            DateRange range = DateRange.of(null, null);

            // Then
            assertTrue(range.isEmpty());
        }

        @Test
        @DisplayName("startDate만 있으면 false")
        void shouldReturnFalseWhenStartDateOnly() {
            // When
            DateRange range = DateRange.from(LocalDate.of(2025, 1, 1));

            // Then
            assertFalse(range.isEmpty());
        }

        @Test
        @DisplayName("endDate만 있으면 false")
        void shouldReturnFalseWhenEndDateOnly() {
            // When
            DateRange range = DateRange.until(LocalDate.of(2025, 1, 31));

            // Then
            assertFalse(range.isEmpty());
        }

        @Test
        @DisplayName("둘 다 있으면 false")
        void shouldReturnFalseWhenBothPresent() {
            // When
            DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

            // Then
            assertFalse(range.isEmpty());
        }
    }

    @Nested
    @DisplayName("contains 테스트")
    class ContainsTest {

        @Test
        @DisplayName("범위 내 날짜는 true")
        void shouldReturnTrueForDateInRange() {
            // Given
            DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

            // Then
            assertTrue(range.contains(LocalDate.of(2025, 1, 15)));
        }

        @Test
        @DisplayName("시작일과 같으면 true")
        void shouldReturnTrueForStartDate() {
            // Given
            DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

            // Then
            assertTrue(range.contains(LocalDate.of(2025, 1, 1)));
        }

        @Test
        @DisplayName("종료일과 같으면 true")
        void shouldReturnTrueForEndDate() {
            // Given
            DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

            // Then
            assertTrue(range.contains(LocalDate.of(2025, 1, 31)));
        }

        @Test
        @DisplayName("시작일 이전은 false")
        void shouldReturnFalseForBeforeStartDate() {
            // Given
            DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

            // Then
            assertFalse(range.contains(LocalDate.of(2024, 12, 31)));
        }

        @Test
        @DisplayName("종료일 이후는 false")
        void shouldReturnFalseForAfterEndDate() {
            // Given
            DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

            // Then
            assertFalse(range.contains(LocalDate.of(2025, 2, 1)));
        }

        @Test
        @DisplayName("null 날짜는 false")
        void shouldReturnFalseForNullDate() {
            // Given
            DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

            // Then
            assertFalse(range.contains(null));
        }

        @Test
        @DisplayName("시작일 없으면 종료일까지 모두 포함")
        void shouldIncludeAllDatesUntilEndWhenNoStart() {
            // Given
            DateRange range = DateRange.until(LocalDate.of(2025, 1, 31));

            // Then
            assertTrue(range.contains(LocalDate.of(2020, 1, 1)));
            assertTrue(range.contains(LocalDate.of(2025, 1, 31)));
            assertFalse(range.contains(LocalDate.of(2025, 2, 1)));
        }

        @Test
        @DisplayName("종료일 없으면 시작일부터 모두 포함")
        void shouldIncludeAllDatesFromStartWhenNoEnd() {
            // Given
            DateRange range = DateRange.from(LocalDate.of(2025, 1, 1));

            // Then
            assertFalse(range.contains(LocalDate.of(2024, 12, 31)));
            assertTrue(range.contains(LocalDate.of(2025, 1, 1)));
            assertTrue(range.contains(LocalDate.of(2030, 12, 31)));
        }
    }
}
