package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PaymentResultV1ApiResponse - 결제 결과 응답 DTO.
 *
 * <p>레거시 PaymentResult 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>PG사(PortOne) API를 통해 실제 결제 상태를 확인한 결과입니다.
 *
 * @param success 결제 성공 여부
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.payment.dto.payment.PaymentResult
 */
@Schema(description = "결제 결과 응답")
public record PaymentResultV1ApiResponse(
        @Schema(description = "결제 성공 여부", example = "true") boolean success) {}
