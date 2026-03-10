package com.ryuqq.setof.application.payment.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.dto.bundle.CartPaymentCreationBundle;
import com.ryuqq.setof.application.payment.internal.PaymentPersistFacade.PaymentPersistResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("CartPaymentPersistFacade 단위 테스트")
class CartPaymentPersistFacadeTest {

    @InjectMocks private CartPaymentPersistFacade sut;

    @Mock private PaymentPersistFacade paymentPersistFacade;
    @Mock private CartCheckoutCoordinator cartCheckoutCoordinator;

    @Nested
    @DisplayName("persist() - 장바구니 결제 영속화 + 카트 삭제")
    class PersistTest {

        @Test
        @DisplayName(
                "PaymentPersistFacade.persist 호출 후 CartCheckoutCoordinator.checkoutAndRemove가 호출된다")
        void persist_ValidBundle_CallsBothPersistAndCheckout() {
            // given
            CartPaymentCreationBundle bundle = PaymentCommandFixtures.cartPaymentCreationBundle();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(1L, "cart-uuid-1234", List.of(100L));

            given(paymentPersistFacade.persist(bundle.paymentBundle())).willReturn(persistResult);
            willDoNothing()
                    .given(cartCheckoutCoordinator)
                    .checkoutAndRemove(bundle.cartCheckoutItems());

            // when
            PaymentPersistResult result = sut.persist(bundle);

            // then
            assertThat(result).isNotNull();
            assertThat(result.paymentId()).isEqualTo(persistResult.paymentId());
            assertThat(result.paymentUniqueId()).isEqualTo(persistResult.paymentUniqueId());
            assertThat(result.orderIds()).isEqualTo(persistResult.orderIds());
        }

        @Test
        @DisplayName("PaymentPersistFacade.persist가 CartCheckoutCoordinator보다 먼저 호출된다")
        void persist_PaymentFacadeCalledBeforeCartCoordinator() {
            // given
            CartPaymentCreationBundle bundle = PaymentCommandFixtures.cartPaymentCreationBundle();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(1L, "cart-uuid-1234", List.of(100L));

            given(paymentPersistFacade.persist(bundle.paymentBundle())).willReturn(persistResult);
            willDoNothing()
                    .given(cartCheckoutCoordinator)
                    .checkoutAndRemove(bundle.cartCheckoutItems());

            // when
            sut.persist(bundle);

            // then
            org.mockito.InOrder inOrder =
                    org.mockito.Mockito.inOrder(paymentPersistFacade, cartCheckoutCoordinator);
            inOrder.verify(paymentPersistFacade).persist(bundle.paymentBundle());
            inOrder.verify(cartCheckoutCoordinator).checkoutAndRemove(bundle.cartCheckoutItems());
        }

        @Test
        @DisplayName("paymentBundle의 persist 결과가 그대로 반환된다")
        void persist_ReturnsResultFromPaymentPersistFacade() {
            // given
            CartPaymentCreationBundle bundle =
                    PaymentCommandFixtures.cartPaymentCreationBundleWithMultipleItems();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(99L, "uuid-multi", List.of(300L, 301L, 302L));

            given(paymentPersistFacade.persist(bundle.paymentBundle())).willReturn(persistResult);
            willDoNothing()
                    .given(cartCheckoutCoordinator)
                    .checkoutAndRemove(bundle.cartCheckoutItems());

            // when
            PaymentPersistResult result = sut.persist(bundle);

            // then
            assertThat(result).isSameAs(persistResult);
        }

        @Test
        @DisplayName("PaymentPersistFacade에 paymentBundle이 전달된다")
        void persist_PassesPaymentBundleToFacade() {
            // given
            CartPaymentCreationBundle bundle = PaymentCommandFixtures.cartPaymentCreationBundle();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(1L, "uuid", List.of(100L));

            given(paymentPersistFacade.persist(bundle.paymentBundle())).willReturn(persistResult);
            willDoNothing()
                    .given(cartCheckoutCoordinator)
                    .checkoutAndRemove(bundle.cartCheckoutItems());

            // when
            sut.persist(bundle);

            // then
            then(paymentPersistFacade).should().persist(bundle.paymentBundle());
        }

        @Test
        @DisplayName("CartCheckoutCoordinator에 cartCheckoutItems가 전달된다")
        void persist_PassesCartCheckoutItemsToCoordinator() {
            // given
            CartPaymentCreationBundle bundle = PaymentCommandFixtures.cartPaymentCreationBundle();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(1L, "uuid", List.of(100L));

            given(paymentPersistFacade.persist(bundle.paymentBundle())).willReturn(persistResult);
            willDoNothing()
                    .given(cartCheckoutCoordinator)
                    .checkoutAndRemove(bundle.cartCheckoutItems());

            // when
            sut.persist(bundle);

            // then
            then(cartCheckoutCoordinator).should().checkoutAndRemove(bundle.cartCheckoutItems());
        }
    }
}
