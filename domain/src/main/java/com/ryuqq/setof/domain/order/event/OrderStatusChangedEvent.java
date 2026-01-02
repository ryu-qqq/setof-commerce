package com.ryuqq.setof.domain.order.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderNumber;
import com.ryuqq.setof.domain.order.vo.OrderStatus;
import java.time.Instant;

/**
 * OrderStatusChangedEvent - 주문 상태 변경 이벤트
 *
 * <p>주문 상태가 변경되었을 때 발행됩니다.
 *
 * @param orderId 주문 ID
 * @param orderNumber 주문 번호
 * @param previousStatus 이전 상태
 * @param newStatus 새 상태
 * @param occurredAt 이벤트 발생 시각
 */
public record OrderStatusChangedEvent(
        OrderId orderId,
        OrderNumber orderNumber,
        OrderStatus previousStatus,
        OrderStatus newStatus,
        Instant occurredAt)
        implements DomainEvent {

    /**
     * Static Factory Method - 상태 변경 이벤트 생성
     *
     * @param orderId 주문 ID
     * @param orderNumber 주문 번호
     * @param previousStatus 이전 상태
     * @param newStatus 새 상태
     * @param occurredAt 이벤트 발생 시각
     * @return OrderStatusChangedEvent 인스턴스
     */
    public static OrderStatusChangedEvent of(
            OrderId orderId,
            OrderNumber orderNumber,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            Instant occurredAt) {
        return new OrderStatusChangedEvent(
                orderId, orderNumber, previousStatus, newStatus, occurredAt);
    }
}
