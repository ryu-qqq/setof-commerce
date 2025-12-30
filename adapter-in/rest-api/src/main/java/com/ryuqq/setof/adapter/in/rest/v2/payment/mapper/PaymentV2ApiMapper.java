package com.ryuqq.setof.adapter.in.rest.v2.payment.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.payment.dto.command.ApprovePaymentV2ApiRequest;
import com.ryuqq.setof.application.payment.dto.command.ApprovePaymentCommand;
import org.springframework.stereotype.Component;

/**
 * Payment V2 API Mapper
 *
 * <p>결제 관련 API DTO ↔ Application Command 변환
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>@Component로 DI (Static 금지)
 *   <li>비즈니스 로직 금지 - 순수 변환만
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class PaymentV2ApiMapper {

    /**
     * 결제 승인 요청 → 승인 커맨드 변환
     *
     * @param request API 요청
     * @return ApprovePaymentCommand
     */
    public ApprovePaymentCommand toApproveCommand(ApprovePaymentV2ApiRequest request) {
        return new ApprovePaymentCommand(
                request.paymentId(), request.pgTransactionId(), request.approvedAmount());
    }
}
