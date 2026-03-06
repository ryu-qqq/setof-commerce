package com.ryuqq.setof.domain.shipment.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.OrderItemId;
import com.ryuqq.setof.domain.shipment.id.ShipmentId;
import java.time.Instant;

/**
 * 반송 완료 이벤트.
 *
 * <p>반송이 완료되어 RETURN_COMPLETED 상태로 전이되었을 때 발행됩니다.
 *
 * @param shipmentId 배송 ID
 * @param orderItemId 주문 아이템 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record ReturnCompletedEvent(
        ShipmentId shipmentId, OrderItemId orderItemId, Instant occurredAt)
        implements DomainEvent {}
