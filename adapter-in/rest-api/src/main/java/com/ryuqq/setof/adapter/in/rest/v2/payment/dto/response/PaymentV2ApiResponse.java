package com.ryuqq.setof.adapter.in.rest.v2.payment.dto.response;

import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * 결제 API 응답 DTO
 *
 * @param paymentId 결제 ID (UUID)
 * @param checkoutId 체크아웃 ID (UUID)
 * @param pgProvider PG사
 * @param pgTransactionId PG사 거래 ID
 * @param method 결제 수단
 * @param status 결제 상태
 * @param requestedAmount 요청 금액
 * @param approvedAmount 승인 금액
 * @param refundedAmount 환불 금액
 * @param approvedAt 승인 시각
 * @param cancelledAt 취소 시각
 * @param createdAt 생성 시각
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "결제 응답")
public record PaymentV2ApiResponse(
        @Schema(description = "결제 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
                String paymentId,
        @Schema(description = "체크아웃 ID (UUID)", example = "660e8400-e29b-41d4-a716-446655440001")
                String checkoutId,
        @Schema(description = "PG사", example = "TOSS") String pgProvider,
        @Schema(description = "PG사 거래 ID", example = "toss_txn_123456789") String pgTransactionId,
        @Schema(description = "결제 수단", example = "CARD") String method,
        @Schema(description = "결제 상태", example = "APPROVED") String status,
        @Schema(description = "요청 금액", example = "59800") BigDecimal requestedAmount,
        @Schema(description = "승인 금액", example = "59800") BigDecimal approvedAmount,
        @Schema(description = "환불 금액", example = "0") BigDecimal refundedAmount,
        @Schema(description = "승인 시각") Instant approvedAt,
        @Schema(description = "취소 시각") Instant cancelledAt,
        @Schema(description = "생성 시각") Instant createdAt) {

    /**
     * Application Response → API Response 변환
     *
     * @param response Application 응답
     * @return API 응답
     */
    public static PaymentV2ApiResponse from(PaymentResponse response) {
        return new PaymentV2ApiResponse(
                response.paymentId(),
                response.checkoutId(),
                response.pgProvider(),
                response.pgTransactionId(),
                response.method(),
                response.status(),
                response.requestedAmount(),
                response.approvedAmount(),
                response.refundedAmount(),
                response.approvedAt(),
                response.cancelledAt(),
                response.createdAt());
    }
}
