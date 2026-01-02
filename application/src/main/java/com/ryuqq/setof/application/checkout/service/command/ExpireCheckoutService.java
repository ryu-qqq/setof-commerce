package com.ryuqq.setof.application.checkout.service.command;

import com.ryuqq.setof.application.checkout.manager.command.CheckoutPersistenceManager;
import com.ryuqq.setof.application.checkout.manager.query.CheckoutReadManager;
import com.ryuqq.setof.application.checkout.port.in.command.ExpireCheckoutUseCase;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 체크아웃 만료 Service
 *
 * <p>만료된 체크아웃을 처리합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ExpireCheckoutService implements ExpireCheckoutUseCase {

    private final CheckoutReadManager checkoutReadManager;
    private final CheckoutPersistenceManager checkoutPersistenceManager;

    public ExpireCheckoutService(
            CheckoutReadManager checkoutReadManager,
            CheckoutPersistenceManager checkoutPersistenceManager) {
        this.checkoutReadManager = checkoutReadManager;
        this.checkoutPersistenceManager = checkoutPersistenceManager;
    }

    /**
     * 체크아웃 만료 처리
     *
     * @param checkoutId 체크아웃 ID (UUID String)
     */
    @Override
    @Transactional
    public void expireCheckout(String checkoutId) {
        Checkout checkout = checkoutReadManager.findById(checkoutId);
        Checkout expiredCheckout = checkout.expire();
        checkoutPersistenceManager.persist(expiredCheckout);
    }
}
