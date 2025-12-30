package com.ryuqq.setof.application.checkout.port.out.query;

import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import java.util.Optional;

/**
 * Checkout Query Port (Query)
 *
 * <p>Checkout 조회 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CheckoutQueryPort {

    /**
     * CheckoutId로 Checkout 조회
     *
     * @param checkoutId 체크아웃 ID
     * @return 체크아웃 (Optional)
     */
    Optional<Checkout> findById(CheckoutId checkoutId);

    /**
     * CheckoutId로 Checkout 조회 (필수)
     *
     * @param checkoutId 체크아웃 ID
     * @return 체크아웃
     * @throws com.ryuqq.setof.domain.checkout.exception.CheckoutNotFoundException 체크아웃이 없는 경우
     */
    Checkout getById(CheckoutId checkoutId);
}
