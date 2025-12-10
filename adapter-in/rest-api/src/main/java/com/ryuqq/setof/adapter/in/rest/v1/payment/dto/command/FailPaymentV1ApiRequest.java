package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 결제 실패 처리 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "결제 실패 처리 요청")
public record FailPaymentV1ApiRequest(
        @Schema(description = "결제 고유 ID", example = "payment_unique_123") String paymentUniqueId,
        @Schema(description = "장바구니 ID 목록", example = "[1, 2, 3]") List<Long> cartIds) {}
