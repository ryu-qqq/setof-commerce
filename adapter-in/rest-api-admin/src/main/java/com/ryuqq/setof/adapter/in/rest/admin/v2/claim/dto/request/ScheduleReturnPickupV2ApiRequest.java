package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * ScheduleReturnPickupV2ApiRequest - 방문수거 예약 API Request DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "방문수거 예약 요청")
public record ScheduleReturnPickupV2ApiRequest(
        @Schema(
                        description = "수거 예약 일시",
                        example = "2025-01-15T10:00:00Z",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "수거 예약 일시는 필수입니다")
                @Future(message = "수거 예약 일시는 현재 시간 이후여야 합니다")
                Instant scheduledAt,
        @Schema(
                        description = "수거지 주소",
                        example = "서울특별시 강남구 테헤란로 123, 456호",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "수거지 주소는 필수입니다")
                String pickupAddress,
        @Schema(
                        description = "고객 연락처",
                        example = "010-1234-5678",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String customerPhone) {}
