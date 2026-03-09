package com.ryuqq.setof.application.payment.manager;

import com.ryuqq.setof.application.payment.port.out.query.PaymentDetailQueryPort;
import com.ryuqq.setof.domain.payment.vo.PaymentFullDetail;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * PaymentDetailReadManager - 결제 단건 상세 조회 Manager.
 *
 * <p>결제 단건 상세 조회를 래핑하여 트랜잭션 경계를 설정합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentDetailReadManager {

    private final PaymentDetailQueryPort paymentDetailQueryPort;

    public PaymentDetailReadManager(PaymentDetailQueryPort paymentDetailQueryPort) {
        this.paymentDetailQueryPort = paymentDetailQueryPort;
    }

    /**
     * 결제 ID와 사용자 ID로 결제 전체 상세 조회.
     *
     * @param paymentId 결제 ID
     * @param userId 사용자 ID (본인 확인용)
     * @return 결제 전체 상세 (도메인 VO)
     */
    @Transactional(readOnly = true)
    public PaymentFullDetail fetchPaymentDetail(long paymentId, long userId) {
        return paymentDetailQueryPort.fetchPaymentDetail(paymentId, userId);
    }
}
