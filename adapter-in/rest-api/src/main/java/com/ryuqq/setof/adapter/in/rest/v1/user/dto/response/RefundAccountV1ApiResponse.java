package com.ryuqq.setof.adapter.in.rest.v1.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * RefundAccountV1ApiResponse - 환불 계좌 정보 응답 DTO.
 *
 * <p>레거시 RefundAccountInfo 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param refundAccountId 환불 계좌 ID
 * @param bankName 은행명
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주명
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.user.dto.account.RefundAccountInfo
 */
@Schema(description = "환불 계좌 정보 응답")
public record RefundAccountV1ApiResponse(
        @Schema(description = "환불 계좌 ID", example = "1") long refundAccountId,
        @Schema(description = "은행명", example = "신한은행") String bankName,
        @Schema(description = "계좌번호", example = "110-123-456789") String accountNumber,
        @Schema(description = "예금주명", example = "홍길동") String accountHolderName) {}
