package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 결제 게이트웨이 요청 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "결제 게이트웨이 요청 응답")
public record PaymentGatewayRequestV1ApiResponse(
        @Schema(description = "결제 고유 ID", example = "payment_unique_123") String paymentUniqueId,
        @Schema(description = "결제 ID",
                example = "1") @JsonInclude(JsonInclude.Include.NON_NULL) Long paymentId,
        @Schema(description = "주문 ID 목록",
                example = "[1, 2, 3]") @JsonInclude(JsonInclude.Include.NON_EMPTY) List<Long> orderIds,
        @Schema(description = "예상 마일리지 금액", example = "900.0") Double expectedMileageAmount) {
}
