package com.ryuqq.setof.domain.order.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.setof.commerce.domain.order.OrderFixtures;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OrderItemPrice Value Object 단위 테스트")
class OrderItemPriceTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 값으로 OrderItemPrice를 생성한다")
        void createWithValidValues() {
            // when
            OrderItemPrice price = new OrderItemPrice(50000, 45000, 48000, 3000, List.of());

            // then
            assertThat(price.regularPrice()).isEqualTo(50000);
            assertThat(price.salePrice()).isEqualTo(45000);
            assertThat(price.orderAmount()).isEqualTo(48000);
            assertThat(price.shippingFee()).isEqualTo(3000);
        }

        @Test
        @DisplayName("정가가 음수이면 예외가 발생한다")
        void createWithNegativeRegularPrice_ThrowsException() {
            assertThatThrownBy(() -> new OrderItemPrice(-1, 45000, 48000, 3000, List.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정가는 0 이상이어야 합니다");
        }

        @Test
        @DisplayName("주문금액이 음수이면 예외가 발생한다")
        void createWithNegativeOrderAmount_ThrowsException() {
            assertThatThrownBy(() -> new OrderItemPrice(50000, 45000, -1, 3000, List.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문금액은 0 이상이어야 합니다");
        }

        @Test
        @DisplayName("할인 내역이 null이면 빈 리스트로 처리된다")
        void createWithNullDiscounts() {
            // when
            OrderItemPrice price = new OrderItemPrice(50000, 50000, 53000, 3000, null);

            // then
            assertThat(price.appliedDiscounts()).isEmpty();
        }

        @Test
        @DisplayName("할인 내역 목록이 불변으로 관리된다")
        void discountsAreImmutable() {
            // given
            OrderItemPrice price = OrderFixtures.defaultOrderItemPrice();

            // then
            assertThatThrownBy(
                            () ->
                                    price.appliedDiscounts()
                                            .add(
                                                    AppliedDiscountSnapshot.of(
                                                            9999L, "TEST", 1000, "테스트 할인")))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("비즈니스 계산 메서드 테스트")
    class BusinessCalculationTest {

        @Test
        @DisplayName("totalDiscount()는 모든 할인 금액의 합을 반환한다")
        void totalDiscountReturnsSumOfAllDiscounts() {
            // given
            OrderItemPrice price = OrderFixtures.defaultOrderItemPrice();

            // when
            int total = price.totalDiscount();

            // then
            assertThat(total).isEqualTo(5000);
        }

        @Test
        @DisplayName("할인이 없으면 totalDiscount()는 0을 반환한다")
        void totalDiscountReturnsZeroWithNoDiscounts() {
            // given
            OrderItemPrice price = OrderFixtures.orderItemPriceWithoutDiscount();

            // then
            assertThat(price.totalDiscount()).isEqualTo(0);
        }

        @Test
        @DisplayName("discountRate()는 정가 대비 할인율을 반환한다")
        void discountRateReturnsCorrectRate() {
            // given
            OrderItemPrice price =
                    new OrderItemPrice(
                            50000,
                            45000,
                            45000,
                            0,
                            List.of(AppliedDiscountSnapshot.of(1L, "SELLER", 5000, "셀러 할인")));

            // when
            int rate = price.discountRate();

            // then
            assertThat(rate).isEqualTo(10); // 5000/50000 * 100 = 10%
        }

        @Test
        @DisplayName("정가가 0이면 discountRate()는 0을 반환한다")
        void discountRateReturnsZeroWhenRegularPriceIsZero() {
            // given
            OrderItemPrice price = new OrderItemPrice(0, 0, 0, 0, List.of());

            // then
            assertThat(price.discountRate()).isEqualTo(0);
        }

        @Test
        @DisplayName("discountAmountOf()는 특정 할인 유형의 금액을 반환한다")
        void discountAmountOfReturnsAmountForType() {
            // given
            OrderItemPrice price = OrderFixtures.defaultOrderItemPrice();

            // when
            int amount = price.discountAmountOf("SELLER_INSTANT");

            // then
            assertThat(amount).isEqualTo(5000);
        }

        @Test
        @DisplayName("discountAmountOf()는 존재하지 않는 유형에 대해 0을 반환한다")
        void discountAmountOfReturnsZeroForUnknownType() {
            // given
            OrderItemPrice price = OrderFixtures.defaultOrderItemPrice();

            // then
            assertThat(price.discountAmountOf("UNKNOWN_TYPE")).isEqualTo(0);
        }
    }
}
