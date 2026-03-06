package com.ryuqq.setof.domain.shipment.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.OrderItemId;
import com.ryuqq.setof.domain.shipment.id.ShipmentId;
import com.ryuqq.setof.domain.shipment.vo.TrackingInfo;
import java.time.Instant;

/**
 * 배송 시작 이벤트.
 *
 * <p>배송이 시작되어 PROCESSING 상태로 전이되었을 때 발행됩니다.
 *
 * @param shipmentId 배송 ID
 * @param orderItemId 주문 아이템 ID
 * @param trackingInfo 배송 추적 정보
 * @param occurredAt 이벤트 발생 시각
 */
public record ShipmentStartedEvent(
        ShipmentId shipmentId,
        OrderItemId orderItemId,
        TrackingInfo trackingInfo,
        Instant occurredAt)
        implements DomainEvent {}
