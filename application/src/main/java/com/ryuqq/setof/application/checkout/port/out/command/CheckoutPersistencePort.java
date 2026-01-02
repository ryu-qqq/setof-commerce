package com.ryuqq.setof.application.checkout.port.out.command;

import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;

/**
 * Checkout Persistence Port (Command)
 *
 * <p>Checkout Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CheckoutPersistencePort {

    /**
     * Checkout 저장 (신규 생성 또는 수정)
     *
     * @param checkout 저장할 Checkout (Domain Aggregate)
     * @return 저장된 Checkout의 ID
     */
    CheckoutId persist(Checkout checkout);
}
