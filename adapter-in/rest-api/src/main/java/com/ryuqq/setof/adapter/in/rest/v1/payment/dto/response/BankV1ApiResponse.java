package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BankV1ApiResponse - 은행 정보 응답 DTO.
 *
 * <p>레거시 BankResponse 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>가상계좌 은행 목록, 환불 은행 목록 조회에 공통으로 사용됩니다.
 *
 * @param bankCode 은행 코드
 * @param bankName 은행명
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.payment.dto.refund.BankResponse
 */
@Schema(description = "은행 정보 응답")
public record BankV1ApiResponse(
        @Schema(description = "은행 코드", example = "004") String bankCode,
        @Schema(description = "은행명", example = "KB국민은행") String bankName) {}
