package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DateRange Value Object 테스트")
class DateRangeTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 DateRange를 생성한다")
        void createWithOf() {
            // given
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 12, 31);

            // when
            DateRange dateRange = DateRange.of(start, end);

            // then
            assertThat(dateRange.startDate()).isEqualTo(start);
            assertThat(dateRange.endDate()).isEqualTo(end);
        }

        @Test
        @DisplayName("시작일이 종료일보다 늦으면 예외를 발생시킨다")
        void startAfterEndThrowsException() {
            // given
            LocalDate start = LocalDate.of(2024, 12, 31);
            LocalDate end = LocalDate.of(2024, 1, 1);

            // when & then
            assertThatThrownBy(() -> DateRange.of(start, end))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이전");
        }

        @Test
        @DisplayName("lastDays()로 최근 N일 범위를 생성한다")
        void createLastDays() {
            // when
            DateRange dateRange = DateRange.lastDays(7);

            // then
            assertThat(dateRange.endDate()).isEqualTo(LocalDate.now());
            assertThat(dateRange.startDate()).isEqualTo(LocalDate.now().minusDays(7));
        }

        @Test
        @DisplayName("lastDays()에 음수를 전달하면 예외를 발생시킨다")
        void lastDaysNegativeThrowsException() {
            // when & then
            assertThatThrownBy(() -> DateRange.lastDays(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("0 이상");
        }

        @Test
        @DisplayName("thisMonth()로 이번 달 범위를 생성한다")
        void createThisMonth() {
            // when
            DateRange dateRange = DateRange.thisMonth();
            LocalDate today = LocalDate.now();

            // then
            assertThat(dateRange.startDate()).isEqualTo(today.withDayOfMonth(1));
            assertThat(dateRange.endDate()).isEqualTo(today.withDayOfMonth(today.lengthOfMonth()));
        }

        @Test
        @DisplayName("lastMonth()로 지난 달 범위를 생성한다")
        void createLastMonth() {
            // when
            DateRange dateRange = DateRange.lastMonth();
            LocalDate today = LocalDate.now();
            LocalDate firstDayLastMonth = today.minusMonths(1).withDayOfMonth(1);
            LocalDate lastDayLastMonth = today.withDayOfMonth(1).minusDays(1);

            // then
            assertThat(dateRange.startDate()).isEqualTo(firstDayLastMonth);
            assertThat(dateRange.endDate()).isEqualTo(lastDayLastMonth);
        }

        @Test
        @DisplayName("until()로 종료일까지의 범위를 생성한다")
        void createUntil() {
            // given
            LocalDate endDate = LocalDate.of(2024, 12, 31);

            // when
            DateRange dateRange = DateRange.until(endDate);

            // then
            assertThat(dateRange.startDate()).isNull();
            assertThat(dateRange.endDate()).isEqualTo(endDate);
        }

        @Test
        @DisplayName("from()으로 시작일부터의 범위를 생성한다")
        void createFrom() {
            // given
            LocalDate startDate = LocalDate.of(2024, 1, 1);

            // when
            DateRange dateRange = DateRange.from(startDate);

            // then
            assertThat(dateRange.startDate()).isEqualTo(startDate);
            assertThat(dateRange.endDate()).isNull();
        }
    }

    @Nested
    @DisplayName("Instant 변환 테스트")
    class InstantConversionTest {

        @Test
        @DisplayName("startInstant()는 시작일의 00:00:00을 반환한다")
        void starInstantReturnsStartOfDay() {
            // given
            LocalDate startDate = LocalDate.of(2024, 1, 15);
            DateRange dateRange = DateRange.of(startDate, startDate);

            // when
            var startInstant = dateRange.startInstant();

            // then
            assertThat(startInstant).isNotNull();
        }

        @Test
        @DisplayName("startInstant()는 시작일이 null이면 null을 반환한다")
        void startInstantReturnsNullForNullStart() {
            // given
            DateRange dateRange = DateRange.until(LocalDate.now());

            // then
            assertThat(dateRange.startInstant()).isNull();
        }

        @Test
        @DisplayName("endInstant()는 종료일의 23:59:59.999999999를 반환한다")
        void endInstantReturnsEndOfDay() {
            // given
            LocalDate endDate = LocalDate.of(2024, 1, 15);
            DateRange dateRange = DateRange.of(endDate, endDate);

            // when
            var endInstant = dateRange.endInstant();

            // then
            assertThat(endInstant).isNotNull();
        }

        @Test
        @DisplayName("endInstant()는 종료일이 null이면 null을 반환한다")
        void endInstantReturnsNullForNullEnd() {
            // given
            DateRange dateRange = DateRange.from(LocalDate.now());

            // then
            assertThat(dateRange.endInstant()).isNull();
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isEmpty()는 시작일과 종료일이 모두 null이면 true를 반환한다")
        void isEmptyReturnsTrueWhenBothNull() {
            // given
            DateRange dateRange = new DateRange(null, null);

            // then
            assertThat(dateRange.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("isEmpty()는 날짜가 하나라도 있으면 false를 반환한다")
        void isEmptyReturnsFalseWhenAnyDateExists() {
            // given
            DateRange dateRange = DateRange.from(LocalDate.now());

            // then
            assertThat(dateRange.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("contains 테스트")
    class ContainsTest {

        @Test
        @DisplayName("contains()는 날짜가 범위 내에 있으면 true를 반환한다")
        void containsReturnsTrueForDateInRange() {
            // given
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 12, 31);
            DateRange dateRange = DateRange.of(start, end);

            // when
            boolean contains = dateRange.contains(LocalDate.of(2024, 6, 15));

            // then
            assertThat(contains).isTrue();
        }

        @Test
        @DisplayName("contains()는 시작일을 포함한다")
        void containsIncludesStartDate() {
            // given
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 12, 31);
            DateRange dateRange = DateRange.of(start, end);

            // then
            assertThat(dateRange.contains(start)).isTrue();
        }

        @Test
        @DisplayName("contains()는 종료일을 포함한다")
        void containsIncludesEndDate() {
            // given
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 12, 31);
            DateRange dateRange = DateRange.of(start, end);

            // then
            assertThat(dateRange.contains(end)).isTrue();
        }

        @Test
        @DisplayName("contains()는 범위 밖 날짜에 false를 반환한다")
        void containsReturnsFalseForDateOutOfRange() {
            // given
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 12, 31);
            DateRange dateRange = DateRange.of(start, end);

            // then
            assertThat(dateRange.contains(LocalDate.of(2025, 1, 1))).isFalse();
        }

        @Test
        @DisplayName("contains()는 null 날짜에 false를 반환한다")
        void containsReturnsFalseForNullDate() {
            // given
            DateRange dateRange =
                    DateRange.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

            // then
            assertThat(dateRange.contains(null)).isFalse();
        }

        @Test
        @DisplayName("시작일이 null인 경우 종료일까지 모든 날짜를 포함한다")
        void containsWithNullStartIncludesAllBeforeEnd() {
            // given
            DateRange dateRange = DateRange.until(LocalDate.of(2024, 6, 30));

            // then
            assertThat(dateRange.contains(LocalDate.of(2020, 1, 1))).isTrue();
            assertThat(dateRange.contains(LocalDate.of(2024, 7, 1))).isFalse();
        }

        @Test
        @DisplayName("종료일이 null인 경우 시작일 이후 모든 날짜를 포함한다")
        void containsWithNullEndIncludesAllAfterStart() {
            // given
            DateRange dateRange = DateRange.from(LocalDate.of(2024, 1, 1));

            // then
            assertThat(dateRange.contains(LocalDate.of(2030, 12, 31))).isTrue();
            assertThat(dateRange.contains(LocalDate.of(2023, 12, 31))).isFalse();
        }
    }
}
