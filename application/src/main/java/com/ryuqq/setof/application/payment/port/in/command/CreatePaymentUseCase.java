package com.ryuqq.setof.application.payment.port.in.command;

import com.ryuqq.setof.application.payment.dto.command.CreatePaymentCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentGatewayResult;

/**
 * 직접 구매 결제 준비 UseCase.
 *
 * <p>가격 검증 → 재고 차감 → payment/orders 레코드 생성 → paymentUniqueId 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface CreatePaymentUseCase {

    /**
     * 직접 구매 주문서를 생성합니다.
     *
     * @param command 결제 생성 커맨드
     * @return 결제 준비 결과 (paymentUniqueId 포함)
     */
    PaymentGatewayResult execute(CreatePaymentCommand command);
}
