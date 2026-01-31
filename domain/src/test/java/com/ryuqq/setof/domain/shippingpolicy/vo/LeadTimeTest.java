package com.ryuqq.setof.domain.shippingpolicy.vo;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("LeadTime Value Object 테스트")
class LeadTimeTest {

    @Nested
    @DisplayName("of() - 일반 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 발송 소요일로 LeadTime을 생성한다")
        void createLeadTimeWithValidDays() {
            // given
            LocalTime cutoffTime = LocalTime.of(14, 0);

            // when
            LeadTime leadTime = LeadTime.of(1, 3, cutoffTime);

            // then
            assertThat(leadTime.minDays()).isEqualTo(1);
            assertThat(leadTime.maxDays()).isEqualTo(3);
            assertThat(leadTime.cutoffTime()).isEqualTo(cutoffTime);
        }

        @Test
        @DisplayName("cutoffTime 없이 LeadTime을 생성한다")
        void createLeadTimeWithoutCutoffTime() {
            // when
            LeadTime leadTime = LeadTime.of(2, 5, null);

            // then
            assertThat(leadTime.minDays()).isEqualTo(2);
            assertThat(leadTime.maxDays()).isEqualTo(5);
            assertThat(leadTime.cutoffTime()).isNull();
        }

        @Test
        @DisplayName("최소/최대가 같은 LeadTime을 생성한다")
        void createLeadTimeWithSameMinMax() {
            // when
            LeadTime leadTime = LeadTime.of(3, 3, LocalTime.of(12, 0));

            // then
            assertThat(leadTime.minDays()).isEqualTo(3);
            assertThat(leadTime.maxDays()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("sameDay() - 당일 배송 생성")
    class SameDayTest {

        @Test
        @DisplayName("당일 배송 LeadTime을 생성한다")
        void createSameDayLeadTime() {
            // given
            LocalTime cutoffTime = LocalTime.of(12, 0);

            // when
            LeadTime leadTime = LeadTime.sameDay(cutoffTime);

            // then
            assertThat(leadTime.minDays()).isEqualTo(0);
            assertThat(leadTime.maxDays()).isEqualTo(0);
            assertThat(leadTime.cutoffTime()).isEqualTo(cutoffTime);
        }
    }

    @Nested
    @DisplayName("nextDay() - 익일 배송 생성")
    class NextDayTest {

        @Test
        @DisplayName("익일 배송 LeadTime을 생성한다")
        void createNextDayLeadTime() {
            // given
            LocalTime cutoffTime = LocalTime.of(14, 0);

            // when
            LeadTime leadTime = LeadTime.nextDay(cutoffTime);

            // then
            assertThat(leadTime.minDays()).isEqualTo(1);
            assertThat(leadTime.maxDays()).isEqualTo(1);
            assertThat(leadTime.cutoffTime()).isEqualTo(cutoffTime);
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("minDays가 음수이면 예외가 발생한다")
        void negativeMinDaysThrowsException() {
            // when & then
            assertThatThrownBy(() -> LeadTime.of(-1, 3, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("최소 발송일은 0 이상");
        }

        @Test
        @DisplayName("maxDays가 minDays보다 작으면 예외가 발생한다")
        void maxDaysLessThanMinDaysThrowsException() {
            // when & then
            assertThatThrownBy(() -> LeadTime.of(5, 3, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("최대 발송일은 최소 발송일 이상");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 LeadTime은 동등하다")
        void sameValuesAreEqual() {
            // given
            LocalTime cutoffTime = LocalTime.of(14, 0);
            LeadTime leadTime1 = LeadTime.of(1, 3, cutoffTime);
            LeadTime leadTime2 = LeadTime.of(1, 3, cutoffTime);

            // then
            assertThat(leadTime1).isEqualTo(leadTime2);
            assertThat(leadTime1.hashCode()).isEqualTo(leadTime2.hashCode());
        }

        @Test
        @DisplayName("다른 값의 LeadTime은 동등하지 않다")
        void differentValuesAreNotEqual() {
            // given
            LeadTime leadTime1 = LeadTime.of(1, 3, null);
            LeadTime leadTime2 = LeadTime.of(2, 5, null);

            // then
            assertThat(leadTime1).isNotEqualTo(leadTime2);
        }
    }
}
