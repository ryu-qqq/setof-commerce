package com.ryuqq.setof.application.payment.port.out.command;

import com.ryuqq.setof.domain.order.aggregate.Order;

/**
 * 결제 시점 배송지 스냅샷 저장 Port.
 *
 * <p>Order에서 paymentId + receiverInfo를 추출하여 저장합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ShippingSnapshotCommandPort {

    /**
     * 배송지 스냅샷을 저장합니다.
     *
     * @param order 주문 도메인 객체 (paymentId + receiverInfo 보유)
     */
    void persist(Order order);
}
