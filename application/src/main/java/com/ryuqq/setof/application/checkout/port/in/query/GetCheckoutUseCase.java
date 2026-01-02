package com.ryuqq.setof.application.checkout.port.in.query;

import com.ryuqq.setof.application.checkout.dto.response.CheckoutResponse;

/**
 * 체크아웃 조회 UseCase
 *
 * <p>체크아웃 정보를 조회합니다
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCheckoutUseCase {

    /**
     * 체크아웃 조회
     *
     * @param checkoutId 체크아웃 ID (UUID String)
     * @return 체크아웃 응답
     */
    CheckoutResponse getCheckout(String checkoutId);
}
