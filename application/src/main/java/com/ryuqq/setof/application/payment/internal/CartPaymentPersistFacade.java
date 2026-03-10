package com.ryuqq.setof.application.payment.internal;

import com.ryuqq.setof.application.payment.dto.bundle.CartPaymentCreationBundle;
import com.ryuqq.setof.application.payment.internal.PaymentPersistFacade.PaymentPersistResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * CartPaymentPersistFacade - 장바구니 결제 영속 Facade.
 *
 * <p>PaymentPersistFacade(결제+주문+배송지+환불계좌) + CartCheckoutCoordinator(카트 삭제)를 하나의 @Transactional로
 * 묶습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartPaymentPersistFacade {

    private final PaymentPersistFacade paymentPersistFacade;
    private final CartCheckoutCoordinator cartCheckoutCoordinator;

    public CartPaymentPersistFacade(
            PaymentPersistFacade paymentPersistFacade,
            CartCheckoutCoordinator cartCheckoutCoordinator) {
        this.paymentPersistFacade = paymentPersistFacade;
        this.cartCheckoutCoordinator = cartCheckoutCoordinator;
    }

    /**
     * 결제 영속화 + 카트 삭제를 하나의 트랜잭션으로 수행합니다.
     *
     * @param bundle 장바구니 결제 번들
     * @return 영속화 결과
     */
    @Transactional
    public PaymentPersistResult persist(CartPaymentCreationBundle bundle) {
        PaymentPersistResult result = paymentPersistFacade.persist(bundle.paymentBundle());

        cartCheckoutCoordinator.checkoutAndRemove(bundle.cartCheckoutItems());

        return result;
    }
}
