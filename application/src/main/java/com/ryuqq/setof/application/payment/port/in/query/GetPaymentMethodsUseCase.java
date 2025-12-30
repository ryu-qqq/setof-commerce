package com.ryuqq.setof.application.payment.port.in.query;

import com.ryuqq.setof.application.payment.dto.response.PaymentMethodResponse;
import java.util.List;

/**
 * GetPaymentMethodsUseCase - 결제 수단 목록 조회 UseCase
 *
 * <p>사용 가능한 결제 수단 목록을 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetPaymentMethodsUseCase {

    /**
     * 결제 수단 목록 조회
     *
     * @return 결제 수단 목록
     */
    List<PaymentMethodResponse> getPaymentMethods();
}
