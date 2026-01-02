package com.ryuqq.setof.application.payment.port.out.query;

import com.ryuqq.setof.application.payment.dto.query.GetPaymentsQuery;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import java.util.List;
import java.util.Optional;

/**
 * Payment Query Port (Query)
 *
 * <p>Payment 조회 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface PaymentQueryPort {

    /**
     * PaymentId로 Payment 조회
     *
     * @param paymentId 결제 ID
     * @return 결제 (Optional)
     */
    Optional<Payment> findById(PaymentId paymentId);

    /**
     * PaymentId로 Payment 조회 (필수)
     *
     * @param paymentId 결제 ID
     * @return 결제
     * @throws com.ryuqq.setof.domain.payment.exception.PaymentNotFoundException 결제가 없는 경우
     */
    Payment getById(PaymentId paymentId);

    /**
     * CheckoutId로 Payment 조회
     *
     * @param checkoutId 체크아웃 ID
     * @return 결제 (Optional)
     */
    Optional<Payment> findByCheckoutId(CheckoutId checkoutId);

    /**
     * CheckoutId로 Payment 조회 (필수)
     *
     * @param checkoutId 체크아웃 ID
     * @return 결제
     * @throws com.ryuqq.setof.domain.payment.exception.PaymentNotFoundException 결제가 없는 경우
     */
    Payment getByCheckoutId(CheckoutId checkoutId);

    /**
     * 조건으로 Payment 목록 조회 (커서 기반 페이징)
     *
     * <p>Slice 방식으로 limit + 1 조회하여 hasNext 판단
     *
     * @param query 조회 조건
     * @return Payment 목록
     */
    List<Payment> findByQuery(GetPaymentsQuery query);

    /**
     * Legacy Payment ID로 Payment 조회
     *
     * <p>V1 API 호환을 위한 Legacy ID 조회 메서드
     *
     * @param legacyPaymentId Legacy Payment ID (Long)
     * @return 결제 (Optional)
     */
    Optional<Payment> findByLegacyPaymentId(Long legacyPaymentId);
}
