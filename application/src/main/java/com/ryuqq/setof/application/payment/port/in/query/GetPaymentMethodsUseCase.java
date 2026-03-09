package com.ryuqq.setof.application.payment.port.in.query;

import com.ryuqq.setof.application.payment.dto.response.PaymentMethodResult;
import java.util.List;

/**
 * GetPaymentMethodsUseCase - 활성 결제 수단 목록 조회 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetPaymentMethodsUseCase {

    /**
     * 활성화된 결제 수단 목록 조회.
     *
     * @return 결제 수단 목록
     */
    List<PaymentMethodResult> execute();
}
