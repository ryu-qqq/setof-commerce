package com.ryuqq.setof.application.payment.internal;

import com.ryuqq.setof.application.order.manager.OrderCompositeReadManager;
import com.ryuqq.setof.application.payment.manager.PaymentCompositeReadManager;
import com.ryuqq.setof.application.payment.manager.PaymentDetailReadManager;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.payment.query.PaymentSearchCriteria;
import com.ryuqq.setof.domain.payment.vo.PaymentFullDetail;
import com.ryuqq.setof.domain.payment.vo.PaymentOverview;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * PaymentReadFacade - 결제 Read Facade.
 *
 * <p>PaymentCompositeReadManager, PaymentDetailReadManager, OrderCompositeReadManager를 래핑하여 결제 조회
 * 결과를 반환합니다. 조립(Assembling)은 Service에서 Assembler를 통해 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentReadFacade {

    private final PaymentCompositeReadManager paymentCompositeReadManager;
    private final PaymentDetailReadManager paymentDetailReadManager;
    private final OrderCompositeReadManager orderCompositeReadManager;

    public PaymentReadFacade(
            PaymentCompositeReadManager paymentCompositeReadManager,
            PaymentDetailReadManager paymentDetailReadManager,
            OrderCompositeReadManager orderCompositeReadManager) {
        this.paymentCompositeReadManager = paymentCompositeReadManager;
        this.paymentDetailReadManager = paymentDetailReadManager;
        this.orderCompositeReadManager = orderCompositeReadManager;
    }

    /**
     * 커서 기반 결제 ID 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 결제 ID 목록 (fetchSize = size + 1)
     */
    @Transactional(readOnly = true)
    public List<Long> fetchPaymentIds(PaymentSearchCriteria criteria) {
        return paymentCompositeReadManager.fetchPaymentIds(criteria);
    }

    /**
     * 결제 ID 목록으로 결제 개요 Composite 조회.
     *
     * @param paymentIds 결제 ID 목록
     * @return 결제 개요 목록 (도메인 VO)
     */
    @Transactional(readOnly = true)
    public List<PaymentOverview> fetchPaymentOverviews(List<Long> paymentIds) {
        if (paymentIds.isEmpty()) {
            return List.of();
        }
        return paymentCompositeReadManager.fetchPaymentOverviews(paymentIds);
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
        return paymentDetailReadManager.fetchPaymentDetail(paymentId, userId);
    }

    /**
     * 주문 ID 목록으로 주문 상세 Composite 조회.
     *
     * <p>결제 상세에서 orderIds를 추출한 뒤, 해당 주문들의 상품/배송/환불 정보를 조회합니다.
     *
     * @param orderIds 주문 ID 목록
     * @return 주문 상세 목록 (도메인 VO)
     */
    @Transactional(readOnly = true)
    public List<OrderDetail> fetchOrderDetails(Set<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return List.of();
        }
        return orderCompositeReadManager.fetchOrderDetails(new ArrayList<>(orderIds));
    }
}
