package com.ryuqq.setof.application.payment.service.query;

import com.ryuqq.setof.application.payment.dto.response.PaymentMethodResponse;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentMethodsUseCase;
import com.ryuqq.setof.domain.payment.vo.PaymentMethod;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetPaymentMethodsService - 결제 수단 목록 조회 서비스
 *
 * <p>사용 가능한 결제 수단 목록을 조회합니다. 결제 수단은 Enum에서 관리됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetPaymentMethodsService implements GetPaymentMethodsUseCase {

    @Override
    public List<PaymentMethodResponse> getPaymentMethods() {
        return Arrays.stream(PaymentMethod.values()).map(PaymentMethodResponse::from).toList();
    }
}
