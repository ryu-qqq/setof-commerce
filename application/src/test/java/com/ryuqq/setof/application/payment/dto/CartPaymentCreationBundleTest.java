package com.ryuqq.setof.application.payment.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.dto.bundle.CartPaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.bundle.PaymentCreationBundle;
import com.ryuqq.setof.domain.cart.vo.CartCheckoutItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartPaymentCreationBundle 단위 테스트")
class CartPaymentCreationBundleTest {

    @Nested
    @DisplayName("생성자 유효성 검증")
    class ValidationTest {

        @Test
        @DisplayName("paymentBundle이 null이면 IllegalArgumentException이 발생한다")
        void constructor_NullPaymentBundle_ThrowsIllegalArgumentException() {
            // given
            List<CartCheckoutItem> cartCheckoutItems =
                    List.of(
                            new CartCheckoutItem(
                                    PaymentCommandFixtures.DEFAULT_CART_ID,
                                    PaymentCommandFixtures.DEFAULT_USER_ID));

            // when & then
            assertThatThrownBy(() -> new CartPaymentCreationBundle(null, cartCheckoutItems))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("paymentBundle");
        }

        @Test
        @DisplayName("cartCheckoutItems가 null이면 IllegalArgumentException이 발생한다")
        void constructor_NullCartCheckoutItems_ThrowsIllegalArgumentException() {
            // given
            PaymentCreationBundle paymentBundle = PaymentCommandFixtures.paymentCreationBundle();

            // when & then
            assertThatThrownBy(() -> new CartPaymentCreationBundle(paymentBundle, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cartCheckoutItems");
        }

        @Test
        @DisplayName("cartCheckoutItems가 빈 리스트이면 IllegalArgumentException이 발생한다")
        void constructor_EmptyCartCheckoutItems_ThrowsIllegalArgumentException() {
            // given
            PaymentCreationBundle paymentBundle = PaymentCommandFixtures.paymentCreationBundle();

            // when & then
            assertThatThrownBy(() -> new CartPaymentCreationBundle(paymentBundle, List.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cartCheckoutItems");
        }

        @Test
        @DisplayName("유효한 인자로 CartPaymentCreationBundle이 정상 생성된다")
        void constructor_ValidArguments_CreatesBundleSuccessfully() {
            // when
            CartPaymentCreationBundle bundle = PaymentCommandFixtures.cartPaymentCreationBundle();

            // then
            assertThat(bundle.paymentBundle()).isNotNull();
            assertThat(bundle.cartCheckoutItems()).isNotEmpty();
        }

        @Test
        @DisplayName("생성 후 cartCheckoutItems는 불변 리스트이다")
        void constructor_CartCheckoutItemsAreImmutable() {
            // when
            CartPaymentCreationBundle bundle = PaymentCommandFixtures.cartPaymentCreationBundle();

            // then
            assertThatThrownBy(
                            () ->
                                    bundle.cartCheckoutItems()
                                            .add(
                                                    new CartCheckoutItem(
                                                            99L,
                                                            PaymentCommandFixtures
                                                                    .DEFAULT_USER_ID)))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("복수의 CartCheckoutItem도 정상 처리된다")
        void constructor_MultipleCartCheckoutItems_CreatesSuccessfully() {
            // when
            CartPaymentCreationBundle bundle =
                    PaymentCommandFixtures.cartPaymentCreationBundleWithMultipleItems();

            // then
            assertThat(bundle.cartCheckoutItems()).hasSize(2);
        }
    }
}
