package com.ryuqq.setof.storage.legacy.paymentmethod.mapper;

import com.ryuqq.setof.application.payment.dto.response.PaymentMethodResult;
import com.ryuqq.setof.storage.legacy.paymentmethod.dto.LegacyPaymentMethodQueryDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyPaymentMethodMapper - 레거시 결제 수단 QueryDto → PaymentMethodResult 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyPaymentMethodMapper {

    /**
     * QueryDto → PaymentMethodResult 변환.
     *
     * @param dto 레거시 조회 DTO
     * @return PaymentMethodResult
     */
    public PaymentMethodResult toResult(LegacyPaymentMethodQueryDto dto) {
        return new PaymentMethodResult(dto.paymentMethod(), dto.displayName(), dto.merchantKey());
    }

    /**
     * QueryDto 목록 → PaymentMethodResult 목록 변환.
     *
     * @param dtos 레거시 조회 DTO 목록
     * @return PaymentMethodResult 목록
     */
    public List<PaymentMethodResult> toResults(List<LegacyPaymentMethodQueryDto> dtos) {
        return dtos.stream().map(this::toResult).toList();
    }
}
