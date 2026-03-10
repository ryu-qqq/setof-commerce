package com.ryuqq.setof.application.payment.port.in.command;

import com.ryuqq.setof.application.payment.dto.command.CreatePaymentInCartCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentGatewayResult;

/**
 * 장바구니 구매 결제 준비 UseCase.
 *
 * <p>직접 구매와 동일한 흐름 + 결제 성공 시 장바구니 아이템 소프트 삭제.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface CreatePaymentInCartUseCase {

    /**
     * 장바구니 구매 주문서를 생성합니다.
     *
     * @param command 장바구니 결제 생성 커맨드
     * @return 결제 준비 결과 (paymentUniqueId 포함)
     */
    PaymentGatewayResult execute(CreatePaymentInCartCommand command);
}
