package com.ryuqq.setof.batch.legacy.shipment.dto;

import java.time.LocalDateTime;

/**
 * 배송완료된 Shipment 정보
 *
 * @param orderId 주문 ID
 * @param deliveryCompletedDate 배송완료 일시
 */
public record DeliveryCompletedShipment(Long orderId, LocalDateTime deliveryCompletedDate) {}
