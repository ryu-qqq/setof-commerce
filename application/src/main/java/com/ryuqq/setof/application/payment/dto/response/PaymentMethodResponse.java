package com.ryuqq.setof.application.payment.dto.response;

import com.ryuqq.setof.domain.payment.vo.PaymentMethod;

/**
 * PaymentMethodResponse - 결제 수단 응답 DTO
 *
 * <p>사용 가능한 결제 수단 정보를 담습니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public record PaymentMethodResponse(String code, String displayName, boolean isEasyPay) {

    /**
     * PaymentMethod enum에서 응답 DTO 생성
     *
     * @param method PaymentMethod enum
     * @return PaymentMethodResponse
     */
    public static PaymentMethodResponse from(PaymentMethod method) {
        return new PaymentMethodResponse(method.name(), method.description(), method.isEasyPay());
    }
}
