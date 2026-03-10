package com.ryuqq.setof.domain.payment.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("UsedMileage Value Object 단위 테스트")
class UsedMileageTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("양수 마일리지로 생성한다")
        void createWithPositiveAmount() {
            // when
            UsedMileage mileage = UsedMileage.of(5000L);

            // then
            assertThat(mileage.amount()).isEqualTo(5000L);
        }

        @Test
        @DisplayName("0 마일리지로 생성한다")
        void createWithZeroAmount() {
            // when
            UsedMileage mileage = UsedMileage.of(0L);

            // then
            assertThat(mileage.amount()).isEqualTo(0L);
            assertThat(mileage.isUsed()).isFalse();
        }

        @Test
        @DisplayName("zero() 팩토리 메서드로 0 마일리지를 생성한다")
        void createWithZeroFactory() {
            // when
            UsedMileage mileage = UsedMileage.zero();

            // then
            assertThat(mileage.amount()).isEqualTo(0L);
        }

        @Test
        @DisplayName("음수 마일리지로 생성하면 예외가 발생한다")
        void createWithNegativeAmount_ThrowsException() {
            assertThatThrownBy(() -> UsedMileage.of(-1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("마일리지는 0 이상이어야 합니다");
        }
    }

    @Nested
    @DisplayName("비즈니스 메서드 테스트")
    class BusinessMethodTest {

        @Test
        @DisplayName("마일리지가 0보다 크면 isUsed()가 true를 반환한다")
        void isUsedReturnsTrueWhenPositive() {
            assertThat(UsedMileage.of(1000L).isUsed()).isTrue();
        }

        @Test
        @DisplayName("마일리지가 0이면 isUsed()가 false를 반환한다")
        void isUsedReturnsFalseWhenZero() {
            assertThat(UsedMileage.zero().isUsed()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 마일리지 금액은 동일하다")
        void sameAmountAreEqual() {
            // given
            UsedMileage m1 = UsedMileage.of(3000L);
            UsedMileage m2 = UsedMileage.of(3000L);

            // then
            assertThat(m1).isEqualTo(m2);
            assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
        }
    }
}
