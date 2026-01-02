package com.ryuqq.setof.domain.order.event;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import com.ryuqq.setof.domain.order.vo.OrderNumber;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import java.time.Instant;

/**
 * OrderCreatedEvent - 주문 생성 이벤트
 *
 * <p>결제 완료 후 주문이 생성되었을 때 발행됩니다.
 *
 * @param orderId 주문 ID
 * @param orderNumber 주문 번호
 * @param checkoutId 결제 세션 ID
 * @param paymentId 결제 ID
 * @param sellerId 판매자 ID
 * @param memberId 회원 ID (UUIDv7 String)
 * @param totalAmount 총 주문 금액
 * @param occurredAt 이벤트 발생 시각
 */
public record OrderCreatedEvent(
        OrderId orderId,
        OrderNumber orderNumber,
        CheckoutId checkoutId,
        PaymentId paymentId,
        Long sellerId,
        String memberId,
        OrderMoney totalAmount,
        Instant occurredAt)
        implements DomainEvent {

    /**
     * Static Factory Method - Order Aggregate로부터 이벤트 생성
     *
     * @param order Order Aggregate
     * @param occurredAt 이벤트 발생 시각
     * @return OrderCreatedEvent 인스턴스
     */
    public static OrderCreatedEvent from(Order order, Instant occurredAt) {
        return new OrderCreatedEvent(
                order.id(),
                order.orderNumber(),
                order.checkoutId(),
                order.paymentId(),
                order.sellerId(),
                order.memberId(),
                order.totalAmount(),
                occurredAt);
    }
}
