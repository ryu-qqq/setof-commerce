package com.ryuqq.setof.application.shipment.dto.command;

import java.time.Instant;

/**
 * 운송장 추적 정보 업데이트 Command DTO
 *
 * <p>스마트택배 API로부터 받은 추적 정보를 전달합니다.
 *
 * @param location 현재 위치
 * @param message 추적 메시지
 * @param trackedAt 추적 시각
 * @param newStatus 새 배송 상태 (nullable - 상태 변경이 없으면 null)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateTrackingCommand(
        String location, String message, Instant trackedAt, String newStatus) {}
