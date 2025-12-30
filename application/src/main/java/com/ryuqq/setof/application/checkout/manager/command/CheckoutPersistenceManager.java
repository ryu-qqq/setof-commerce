package com.ryuqq.setof.application.checkout.manager.command;

import com.ryuqq.setof.application.checkout.port.out.command.CheckoutPersistencePort;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Checkout Persistence Manager
 *
 * <p>Checkout 영속화 관리자
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CheckoutPersistenceManager {

    private final CheckoutPersistencePort checkoutPersistencePort;

    public CheckoutPersistenceManager(CheckoutPersistencePort checkoutPersistencePort) {
        this.checkoutPersistencePort = checkoutPersistencePort;
    }

    /**
     * Checkout 저장
     *
     * @param checkout 저장할 Checkout
     * @return 저장된 Checkout ID
     */
    @Transactional
    public CheckoutId persist(Checkout checkout) {
        return checkoutPersistencePort.persist(checkout);
    }
}
