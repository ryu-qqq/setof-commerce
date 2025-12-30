package com.ryuqq.setof.domain.order.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import com.ryuqq.setof.domain.order.vo.OrderNumber;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import java.time.Instant;

/**
 * OrderCancelledEvent - 주문 취소 이벤트
 *
 * <p>주문이 취소되었을 때 발행됩니다.
 *
 * @param orderId 주문 ID
 * @param orderNumber 주문 번호
 * @param paymentId 결제 ID
 * @param cancelledAmount 취소 금액
 * @param occurredAt 이벤트 발생 시각
 */
public record OrderCancelledEvent(
        OrderId orderId,
        OrderNumber orderNumber,
        PaymentId paymentId,
        OrderMoney cancelledAmount,
        Instant occurredAt)
        implements DomainEvent {

    /**
     * Static Factory Method - Order Aggregate로부터 이벤트 생성
     *
     * @param order Order Aggregate
     * @param occurredAt 이벤트 발생 시각
     * @return OrderCancelledEvent 인스턴스
     */
    public static OrderCancelledEvent from(Order order, Instant occurredAt) {
        return new OrderCancelledEvent(
                order.id(),
                order.orderNumber(),
                order.paymentId(),
                order.totalAmount(),
                occurredAt);
    }
}
