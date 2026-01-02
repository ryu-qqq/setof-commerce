package com.ryuqq.setof.application.shipment.dto.command;

/**
 * 운송장 상태 변경 Command DTO
 *
 * @param status 새 배송 상태 (PENDING, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, CANCELLED)
 * @author development-team
 * @since 1.0.0
 */
public record ChangeShipmentStatusCommand(String status) {}
