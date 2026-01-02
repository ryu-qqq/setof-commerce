package com.ryuqq.setof.domain.discount.vo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.discount.exception.InvalidValidPeriodException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** ValidPeriod Value Object 테스트 */
@DisplayName("ValidPeriod Value Object")
class ValidPeriodTest {

    private static final Instant NOW = Instant.now();
    private static final Instant PAST = NOW.minus(10, ChronoUnit.DAYS);
    private static final Instant FUTURE = NOW.plus(10, ChronoUnit.DAYS);

    @Nested
    @DisplayName("생성 검증")
    class Creation {

        @Test
        @DisplayName("유효한 기간을 생성할 수 있다")
        void shouldCreateValidPeriod() {
            // when
            ValidPeriod period = ValidPeriod.of(NOW, FUTURE);

            // then
            assertTrue(period.startAt().isBefore(period.endAt()));
        }

        @Test
        @DisplayName("시작일이 null이면 예외가 발생한다")
        void shouldThrowExceptionForNullStartAt() {
            // when & then
            assertThrows(InvalidValidPeriodException.class, () -> ValidPeriod.of(null, FUTURE));
        }

        @Test
        @DisplayName("종료일이 null이면 예외가 발생한다")
        void shouldThrowExceptionForNullEndAt() {
            // when & then
            assertThrows(InvalidValidPeriodException.class, () -> ValidPeriod.of(NOW, null));
        }

        @Test
        @DisplayName("시작일이 종료일보다 늦으면 예외가 발생한다")
        void shouldThrowExceptionWhenStartAfterEnd() {
            // when & then
            assertThrows(InvalidValidPeriodException.class, () -> ValidPeriod.of(FUTURE, NOW));
        }

        @Test
        @DisplayName("무제한 기간을 생성할 수 있다")
        void shouldCreateUnlimitedPeriod() {
            // when
            ValidPeriod period = ValidPeriod.unlimited();

            // then
            assertTrue(period.isCurrentlyValid());
        }
    }

    @Nested
    @DisplayName("기간 내 확인")
    class WithinPeriod {

        @Test
        @DisplayName("현재가 유효 기간 내인지 확인할 수 있다")
        void shouldCheckIfCurrentlyValid() {
            // given
            ValidPeriod validPeriod = ValidPeriod.of(PAST, FUTURE);
            ValidPeriod expiredPeriod =
                    ValidPeriod.of(
                            PAST.minus(20, ChronoUnit.DAYS), PAST.minus(10, ChronoUnit.DAYS));
            ValidPeriod futurePeriod =
                    ValidPeriod.of(
                            FUTURE.plus(10, ChronoUnit.DAYS), FUTURE.plus(20, ChronoUnit.DAYS));

            // then
            assertTrue(validPeriod.isCurrentlyValid());
            assertFalse(expiredPeriod.isCurrentlyValid());
            assertFalse(futurePeriod.isCurrentlyValid());
        }

        @Test
        @DisplayName("특정 시점이 기간 내인지 확인할 수 있다")
        void shouldCheckIfWithinPeriod() {
            // given
            ValidPeriod period = ValidPeriod.of(PAST, FUTURE);

            // then
            assertTrue(period.isWithinPeriod(NOW));
            assertTrue(period.isWithinPeriod(PAST)); // 경계 포함
            assertTrue(period.isWithinPeriod(FUTURE)); // 경계 포함
            assertFalse(period.isWithinPeriod(PAST.minus(1, ChronoUnit.DAYS)));
            assertFalse(period.isWithinPeriod(FUTURE.plus(1, ChronoUnit.DAYS)));
        }
    }

    @Nested
    @DisplayName("만료/시작 전 확인")
    class ExpiredNotStarted {

        @Test
        @DisplayName("만료 여부를 확인할 수 있다")
        void shouldCheckIfExpired() {
            // given
            ValidPeriod expiredPeriod =
                    ValidPeriod.of(
                            PAST.minus(20, ChronoUnit.DAYS), PAST.minus(10, ChronoUnit.DAYS));
            ValidPeriod activePeriod = ValidPeriod.of(PAST, FUTURE);

            // then
            assertTrue(expiredPeriod.isExpired());
            assertFalse(activePeriod.isExpired());
        }

        @Test
        @DisplayName("시작 전 여부를 확인할 수 있다")
        void shouldCheckIfNotStarted() {
            // given
            ValidPeriod futurePeriod =
                    ValidPeriod.of(
                            FUTURE.plus(10, ChronoUnit.DAYS), FUTURE.plus(20, ChronoUnit.DAYS));
            ValidPeriod activePeriod = ValidPeriod.of(PAST, FUTURE);

            // then
            assertTrue(futurePeriod.isNotStarted());
            assertFalse(activePeriod.isNotStarted());
        }
    }

    @Nested
    @DisplayName("기간 겹침 확인")
    class Overlaps {

        @Test
        @DisplayName("겹치는 기간을 감지할 수 있다")
        void shouldDetectOverlappingPeriods() {
            // given
            ValidPeriod period1 = ValidPeriod.of(PAST, NOW);
            ValidPeriod period2 = ValidPeriod.of(NOW.minus(5, ChronoUnit.DAYS), FUTURE);

            // then
            assertTrue(period1.overlaps(period2));
            assertTrue(period2.overlaps(period1));
        }

        @Test
        @DisplayName("겹치지 않는 기간을 확인할 수 있다")
        void shouldDetectNonOverlappingPeriods() {
            // given
            ValidPeriod period1 =
                    ValidPeriod.of(
                            PAST.minus(20, ChronoUnit.DAYS), PAST.minus(10, ChronoUnit.DAYS));
            ValidPeriod period2 = ValidPeriod.of(NOW, FUTURE);

            // then
            assertFalse(period1.overlaps(period2));
            assertFalse(period2.overlaps(period1));
        }
    }
}
