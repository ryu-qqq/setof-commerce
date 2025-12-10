package com.ryuqq.setof.adapter.in.rest.v1.order.dto.command;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 일반 주문 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@JsonTypeName("normalOrder")
@Schema(description = "일반 주문 수정 요청")
public record NormalOrderV1ApiRequest(
        @Schema(description = "결제 ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "결제 ID는 필수입니다.") Long paymentId,
        @Schema(description = "주문 ID", example = "1") Long orderId,
        @Schema(description = "주문 상태", example = "CANCELLED",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "주문 상태는 필수입니다.") String orderStatus,
        @Schema(description = "주문 스냅샷 저장 여부", example = "true") Boolean saveOrderSnapShot)
        implements UpdateOrderV1ApiRequest {

    @Override
    public String changeReason() {
        return "";
    }

    @Override
    public String changeDetailReason() {
        return "";
    }
}
