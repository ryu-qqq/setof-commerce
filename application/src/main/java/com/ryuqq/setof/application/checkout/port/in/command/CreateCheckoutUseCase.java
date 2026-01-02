package com.ryuqq.setof.application.checkout.port.in.command;

import com.ryuqq.setof.application.checkout.dto.command.CreateCheckoutCommand;
import com.ryuqq.setof.application.checkout.dto.response.CheckoutResponse;

/**
 * 체크아웃 생성 UseCase
 *
 * <p>구매 결제 세션을 생성합니다
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateCheckoutUseCase {

    /**
     * 체크아웃 생성
     *
     * @param command 체크아웃 생성 Command
     * @return 생성된 체크아웃 응답
     */
    CheckoutResponse createCheckout(CreateCheckoutCommand command);
}
