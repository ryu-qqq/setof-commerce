package com.ryuqq.setof.application.claim.dto.command;

import java.time.Instant;

/**
 * ScheduleReturnPickupCommand - 반품 수거 예약 Command
 *
 * <p>판매자가 고객에게 방문수거를 예약할 때 사용합니다.
 *
 * @param claimId 클레임 ID
 * @param scheduledAt 수거 예약 일시
 * @param pickupAddress 수거지 주소
 * @param customerPhone 고객 연락처
 * @author development-team
 * @since 2.0.0
 */
public record ScheduleReturnPickupCommand(
        String claimId, Instant scheduledAt, String pickupAddress, String customerPhone) {}
