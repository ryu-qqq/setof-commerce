package com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 체크아웃 완료 API 요청 DTO
 *
 * <p>PG 결제 완료 후 체크아웃을 완료 처리하기 위한 요청입니다.
 *
 * @param paymentId 결제 ID (UUID)
 * @param pgTransactionId PG사 거래 ID
 * @param approvedAmount 승인 금액
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "체크아웃 완료 요청")
public record CompleteCheckoutV2ApiRequest(
        @Schema(description = "결제 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
                @NotBlank(message = "결제 ID는 필수입니다")
                String paymentId,
        @Schema(description = "PG사 거래 ID", example = "toss_txn_123456789")
                @NotBlank(message = "PG사 거래 ID는 필수입니다")
                String pgTransactionId,
        @Schema(description = "승인 금액", example = "59800")
                @NotNull(message = "승인 금액은 필수입니다")
                @Positive(message = "승인 금액은 0보다 커야 합니다")
                BigDecimal approvedAmount) {}
