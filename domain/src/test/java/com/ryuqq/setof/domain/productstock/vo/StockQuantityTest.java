package com.ryuqq.setof.domain.productstock.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productstock.exception.NotEnoughStockException;
import com.ryuqq.setof.domain.productstock.exception.StockOverflowException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("StockQuantity Value Object 테스트")
class StockQuantityTest {

    private static final Long PRODUCT_ID = 100L;

    @Nested
    @DisplayName("of() 테스트")
    class OfTest {

        @Test
        @DisplayName("유효한 수량으로 생성 성공")
        void shouldCreateWithValidQuantity() {
            // given
            int value = 100;

            // when
            StockQuantity quantity = StockQuantity.of(value);

            // then
            assertThat(quantity.value()).isEqualTo(100);
        }

        @Test
        @DisplayName("0으로 생성 성공")
        void shouldCreateWithZero() {
            // when
            StockQuantity quantity = StockQuantity.of(0);

            // then
            assertThat(quantity.value()).isEqualTo(0);
        }

        @Test
        @DisplayName("음수로 생성 시 예외 발생")
        void shouldThrowWhenNegative() {
            // when & then
            assertThatThrownBy(() -> StockQuantity.of(-1))
                    .isInstanceOf(NotEnoughStockException.class);
        }
    }

    @Nested
    @DisplayName("zero() 테스트")
    class ZeroTest {

        @Test
        @DisplayName("0 수량 생성 성공")
        void shouldCreateZeroQuantity() {
            // when
            StockQuantity quantity = StockQuantity.zero();

            // then
            assertThat(quantity.value()).isEqualTo(0);
            assertThat(quantity.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("hasStock() / isEmpty() 테스트")
    class StockStatusTest {

        @Test
        @DisplayName("양수 수량이면 hasStock() true, isEmpty() false")
        void shouldReturnTrueWhenPositive() {
            // given
            StockQuantity quantity = StockQuantity.of(10);

            // when & then
            assertThat(quantity.hasStock()).isTrue();
            assertThat(quantity.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("0이면 hasStock() false, isEmpty() true")
        void shouldReturnFalseWhenZero() {
            // given
            StockQuantity quantity = StockQuantity.zero();

            // when & then
            assertThat(quantity.hasStock()).isFalse();
            assertThat(quantity.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("isEnough() 테스트")
    class IsEnoughTest {

        @Test
        @DisplayName("충분한 재고이면 true 반환")
        void shouldReturnTrueWhenEnough() {
            // given
            StockQuantity quantity = StockQuantity.of(100);

            // when & then
            assertThat(quantity.isEnough(50)).isTrue();
            assertThat(quantity.isEnough(100)).isTrue();
        }

        @Test
        @DisplayName("부족한 재고이면 false 반환")
        void shouldReturnFalseWhenNotEnough() {
            // given
            StockQuantity quantity = StockQuantity.of(10);

            // when & then
            assertThat(quantity.isEnough(20)).isFalse();
        }
    }

    @Nested
    @DisplayName("deduct() 테스트")
    class DeductTest {

        @Test
        @DisplayName("재고 차감 성공")
        void shouldDeductSuccessfully() {
            // given
            StockQuantity quantity = StockQuantity.of(100);

            // when
            StockQuantity deducted = quantity.deduct(30, PRODUCT_ID);

            // then
            assertThat(deducted.value()).isEqualTo(70);
            assertThat(quantity.value()).isEqualTo(100); // 원본 불변
        }

        @Test
        @DisplayName("전체 재고 차감 성공")
        void shouldDeductAll() {
            // given
            StockQuantity quantity = StockQuantity.of(50);

            // when
            StockQuantity deducted = quantity.deduct(50, PRODUCT_ID);

            // then
            assertThat(deducted.value()).isEqualTo(0);
        }

        @Test
        @DisplayName("재고 부족 시 예외 발생")
        void shouldThrowWhenNotEnough() {
            // given
            StockQuantity quantity = StockQuantity.of(10);

            // when & then
            assertThatThrownBy(() -> quantity.deduct(20, PRODUCT_ID))
                    .isInstanceOf(NotEnoughStockException.class);
        }

        @Test
        @DisplayName("음수 차감 시 예외 발생")
        void shouldThrowWhenDeductNegative() {
            // given
            StockQuantity quantity = StockQuantity.of(100);

            // when & then
            assertThatThrownBy(() -> quantity.deduct(-10, PRODUCT_ID))
                    .isInstanceOf(NotEnoughStockException.class);
        }
    }

    @Nested
    @DisplayName("restore() 테스트")
    class RestoreTest {

        @Test
        @DisplayName("재고 복원 성공")
        void shouldRestoreSuccessfully() {
            // given
            StockQuantity quantity = StockQuantity.of(50);

            // when
            StockQuantity restored = quantity.restore(30, PRODUCT_ID);

            // then
            assertThat(restored.value()).isEqualTo(80);
            assertThat(quantity.value()).isEqualTo(50); // 원본 불변
        }

        @Test
        @DisplayName("0에서 복원 성공")
        void shouldRestoreFromZero() {
            // given
            StockQuantity quantity = StockQuantity.zero();

            // when
            StockQuantity restored = quantity.restore(100, PRODUCT_ID);

            // then
            assertThat(restored.value()).isEqualTo(100);
        }

        @Test
        @DisplayName("최대값 초과 시 예외 발생")
        void shouldThrowWhenOverflow() {
            // given
            StockQuantity quantity = StockQuantity.of(Integer.MAX_VALUE - 10);

            // when & then
            assertThatThrownBy(() -> quantity.restore(20, PRODUCT_ID))
                    .isInstanceOf(StockOverflowException.class);
        }

        @Test
        @DisplayName("음수 복원 시 예외 발생")
        void shouldThrowWhenRestoreNegative() {
            // given
            StockQuantity quantity = StockQuantity.of(100);

            // when & then
            assertThatThrownBy(() -> quantity.restore(-10, PRODUCT_ID))
                    .isInstanceOf(StockOverflowException.class);
        }
    }

    @Nested
    @DisplayName("set() 테스트")
    class SetTest {

        @Test
        @DisplayName("수량 설정 성공")
        void shouldSetSuccessfully() {
            // given
            StockQuantity quantity = StockQuantity.of(50);

            // when
            StockQuantity updated = quantity.set(200);

            // then
            assertThat(updated.value()).isEqualTo(200);
            assertThat(quantity.value()).isEqualTo(50); // 원본 불변
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동등")
        void shouldBeEqualWhenSameValue() {
            // given
            StockQuantity q1 = StockQuantity.of(100);
            StockQuantity q2 = StockQuantity.of(100);

            // when & then
            assertThat(q1).isEqualTo(q2);
            assertThat(q1.hashCode()).isEqualTo(q2.hashCode());
        }

        @Test
        @DisplayName("다른 값이면 동등하지 않음")
        void shouldNotBeEqualWhenDifferentValue() {
            // given
            StockQuantity q1 = StockQuantity.of(100);
            StockQuantity q2 = StockQuantity.of(200);

            // when & then
            assertThat(q1).isNotEqualTo(q2);
        }
    }
}
