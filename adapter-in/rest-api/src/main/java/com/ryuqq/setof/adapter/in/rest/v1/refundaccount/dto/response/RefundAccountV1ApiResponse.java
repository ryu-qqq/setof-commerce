package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * RefundAccountV1ApiResponse - 환불 계좌 고객용 응답 DTO.
 *
 * <p>레거시 RefundAccountInfo 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>API-DTO-005: 레거시 JSON 응답 구조 호환 (Shadow Traffic 비교용).
 *
 * <p>레거시 응답 구조:
 *
 * <pre>
 * {
 *   "refundAccountId": 1,
 *   "bankName": "국민은행",
 *   "accountNumber": "123456789012",
 *   "accountHolderName": "홍길동"
 * }
 * </pre>
 *
 * @param refundAccountId 환불 계좌 ID
 * @param bankName 은행명
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주명
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "환불 계좌 응답 (고객용)")
public record RefundAccountV1ApiResponse(
        @Schema(description = "환불 계좌 ID", example = "1") long refundAccountId,
        @Schema(description = "은행명", example = "국민은행") String bankName,
        @Schema(description = "계좌번호", example = "123456789012") String accountNumber,
        @Schema(description = "예금주명", example = "홍길동") String accountHolderName) {}
