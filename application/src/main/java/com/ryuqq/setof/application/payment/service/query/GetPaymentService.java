package com.ryuqq.setof.application.payment.service.query;

import com.ryuqq.setof.application.payment.assembler.PaymentAssembler;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import com.ryuqq.setof.application.payment.manager.query.PaymentReadManager;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentUseCase;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 결제 조회 Service
 *
 * <p>결제 정보를 조회합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetPaymentService implements GetPaymentUseCase {

    private final PaymentReadManager paymentReadManager;
    private final PaymentAssembler paymentAssembler;

    public GetPaymentService(
            PaymentReadManager paymentReadManager, PaymentAssembler paymentAssembler) {
        this.paymentReadManager = paymentReadManager;
        this.paymentAssembler = paymentAssembler;
    }

    /**
     * 결제 조회
     *
     * @param paymentId 결제 ID (UUID String)
     * @return 결제 응답
     */
    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(String paymentId) {
        Payment payment = paymentReadManager.findById(paymentId);
        return paymentAssembler.toResponse(payment);
    }

    /**
     * 체크아웃 ID로 결제 조회
     *
     * @param checkoutId 체크아웃 ID (UUID String)
     * @return 결제 응답
     */
    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByCheckoutId(String checkoutId) {
        Payment payment = paymentReadManager.findByCheckoutId(checkoutId);
        return paymentAssembler.toResponse(payment);
    }

    /**
     * Legacy Payment ID로 결제 조회
     *
     * <p>V1 API 호환을 위한 Legacy ID 기반 조회
     *
     * @param legacyPaymentId Legacy Payment ID (Long)
     * @return 결제 응답
     */
    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByLegacyId(Long legacyPaymentId) {
        Payment payment = paymentReadManager.findByLegacyId(legacyPaymentId);
        return paymentAssembler.toResponse(payment);
    }
}
