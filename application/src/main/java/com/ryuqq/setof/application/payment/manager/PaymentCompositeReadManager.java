package com.ryuqq.setof.application.payment.manager;

import com.ryuqq.setof.application.payment.port.out.query.PaymentCompositeQueryPort;
import com.ryuqq.setof.domain.payment.query.PaymentSearchCriteria;
import com.ryuqq.setof.domain.payment.vo.PaymentOverview;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * PaymentCompositeReadManager - 결제 Composite 조회 Manager.
 *
 * <p>커서 기반 ID 조회와 다중 테이블 JOIN이 필요한 결제 목록 조회를 래핑하여 트랜잭션 경계를 설정합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentCompositeReadManager {

    private final PaymentCompositeQueryPort paymentCompositeQueryPort;

    public PaymentCompositeReadManager(PaymentCompositeQueryPort paymentCompositeQueryPort) {
        this.paymentCompositeQueryPort = paymentCompositeQueryPort;
    }

    /**
     * 검색 조건으로 결제 ID 목록 조회.
     *
     * @param criteria 결제 검색 조건
     * @return 결제 ID 목록 (fetchSize = size + 1)
     */
    @Transactional(readOnly = true)
    public List<Long> fetchPaymentIds(PaymentSearchCriteria criteria) {
        return paymentCompositeQueryPort.fetchPaymentIds(criteria);
    }

    /**
     * 결제 ID 목록으로 결제 개요 Composite 조회.
     *
     * @param paymentIds 결제 ID 목록
     * @return 결제 개요 목록 (도메인 VO)
     */
    @Transactional(readOnly = true)
    public List<PaymentOverview> fetchPaymentOverviews(List<Long> paymentIds) {
        return paymentCompositeQueryPort.fetchPaymentOverviews(paymentIds);
    }

    /**
     * 검색 조건으로 결제 건수 조회 (totalElements 계산용).
     *
     * @param criteria 결제 검색 조건
     * @return 결제 건수
     */
    @Transactional(readOnly = true)
    public long countPayments(PaymentSearchCriteria criteria) {
        return paymentCompositeQueryPort.countPayments(criteria);
    }
}
