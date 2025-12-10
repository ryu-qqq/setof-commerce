package com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 환불계좌 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "환불계좌 정보 응답")
public record RefundAccountV1ApiResponse(
        @Schema(description = "환불계좌 ID", example = "1") Long refundAccountId,
        @Schema(description = "은행명", example = "신한은행") String bankName,
        @Schema(description = "계좌번호", example = "110123456789") String accountNumber,
        @Schema(description = "예금주명", example = "홍길동") String accountHolderName) {
}
