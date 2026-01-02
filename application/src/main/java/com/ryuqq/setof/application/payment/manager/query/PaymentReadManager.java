package com.ryuqq.setof.application.payment.manager.query;

import com.ryuqq.setof.application.payment.dto.query.GetPaymentsQuery;
import com.ryuqq.setof.application.payment.port.out.query.PaymentQueryPort;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Payment Read Manager
 *
 * <p>Payment 조회 관리자
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PaymentReadManager {

    private final PaymentQueryPort paymentQueryPort;

    public PaymentReadManager(PaymentQueryPort paymentQueryPort) {
        this.paymentQueryPort = paymentQueryPort;
    }

    /**
     * PaymentId 문자열로 Payment 조회
     *
     * @param paymentIdString 결제 ID 문자열 (UUID)
     * @return Payment Aggregate
     */
    public Payment findById(String paymentIdString) {
        PaymentId paymentId = PaymentId.of(UUID.fromString(paymentIdString));
        return paymentQueryPort.getById(paymentId);
    }

    /**
     * CheckoutId 문자열로 Payment 조회
     *
     * @param checkoutIdString 체크아웃 ID 문자열 (UUID)
     * @return Payment Aggregate
     */
    public Payment findByCheckoutId(String checkoutIdString) {
        CheckoutId checkoutId = CheckoutId.of(UUID.fromString(checkoutIdString));
        return paymentQueryPort.getByCheckoutId(checkoutId);
    }

    /**
     * 조건으로 Payment 목록 조회 (커서 기반 페이징)
     *
     * <p>Slice 방식으로 limit + 1 조회하여 hasNext 판단
     *
     * @param query 조회 조건
     * @return Payment 목록
     */
    public List<Payment> searchPayments(GetPaymentsQuery query) {
        return paymentQueryPort.findByQuery(query);
    }

    /**
     * Legacy Payment ID로 Payment 조회
     *
     * <p>V1 API 호환을 위한 Legacy ID 기반 조회
     *
     * @param legacyPaymentId Legacy Payment ID (Long)
     * @return Payment Aggregate
     * @throws com.ryuqq.setof.domain.payment.exception.PaymentNotFoundException 결제가 없는 경우
     */
    public Payment findByLegacyId(Long legacyPaymentId) {
        return paymentQueryPort
                .findByLegacyPaymentId(legacyPaymentId)
                .orElseThrow(
                        () ->
                                new com.ryuqq.setof.domain.payment.exception
                                        .PaymentNotFoundException(legacyPaymentId));
    }
}
