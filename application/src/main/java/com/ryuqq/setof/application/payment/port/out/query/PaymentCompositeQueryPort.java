package com.ryuqq.setof.application.payment.port.out.query;

import com.ryuqq.setof.domain.payment.query.PaymentSearchCriteria;
import com.ryuqq.setof.domain.payment.vo.PaymentOverview;
import java.util.List;

/**
 * PaymentCompositeQueryPort - 결제 Composite 조회 Port-Out.
 *
 * <p>커서 기반 결제 ID 조회와 다중 테이블 JOIN이 필요한 결제 목록 조회를 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface PaymentCompositeQueryPort {

    /**
     * 검색 조건으로 결제 ID 목록 조회 (커서 기반 페이징).
     *
     * @param criteria 결제 검색 조건
     * @return 결제 ID 목록 (fetchSize = size + 1)
     */
    List<Long> fetchPaymentIds(PaymentSearchCriteria criteria);

    /**
     * 결제 ID 목록으로 결제 목록 개요 조회 (Composite JOIN).
     *
     * @param paymentIds 결제 ID 목록
     * @return 결제 개요 목록 (도메인 VO)
     */
    List<PaymentOverview> fetchPaymentOverviews(List<Long> paymentIds);

    /**
     * 검색 조건으로 결제 건수 조회 (totalElements 계산용).
     *
     * @param criteria 결제 검색 조건
     * @return 결제 건수
     */
    long countPayments(PaymentSearchCriteria criteria);
}
