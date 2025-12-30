package com.ryuqq.setof.application.payment.assembler;

import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import org.springframework.stereotype.Component;

/**
 * Payment Assembler
 *
 * <p>Domain Aggregate를 Response DTO로 변환하는 Assembler
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PaymentAssembler {

    /**
     * Payment을 PaymentResponse로 변환
     *
     * @param payment Domain Aggregate
     * @return Response DTO
     */
    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.id().value().toString(),
                payment.checkoutId().value().toString(),
                payment.pgProvider().name(),
                payment.pgTransactionId(),
                payment.method().name(),
                payment.status().name(),
                payment.requestedAmount().value(),
                payment.approvedAmount() != null ? payment.approvedAmount().value() : null,
                payment.refundedAmount() != null ? payment.refundedAmount().value() : null,
                payment.approvedAt(),
                payment.cancelledAt(),
                payment.createdAt());
    }
}
