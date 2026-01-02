package com.ryuqq.setof.application.checkout.port.in.command;

/**
 * 체크아웃 만료 UseCase
 *
 * <p>만료된 체크아웃을 처리합니다
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ExpireCheckoutUseCase {

    /**
     * 체크아웃 만료 처리
     *
     * @param checkoutId 체크아웃 ID (UUID String)
     */
    void expireCheckout(String checkoutId);
}
