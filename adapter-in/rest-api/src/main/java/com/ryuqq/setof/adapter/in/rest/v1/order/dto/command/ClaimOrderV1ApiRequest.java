package com.ryuqq.setof.adapter.in.rest.v1.order.dto.command;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 클레임 주문 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@JsonTypeName("claimOrder")
@Schema(description = "클레임 주문 수정 요청")
public record ClaimOrderV1ApiRequest(
        @Schema(description = "결제 ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "결제 ID는 필수입니다.") Long paymentId,
        @Schema(description = "주문 ID", example = "1") Long orderId,
        @Schema(description = "주문 상태", example = "CANCELLED",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "주문 상태는 필수입니다.") String orderStatus,
        @Schema(description = "변경 사유", example = "고객 요청") @Length(max = 200,
                message = "사유는 200자를 넘어갈 수 없습니다.") String changeReason,
        @Schema(description = "변경 상세 사유", example = "단순 변심") @Length(max = 500,
                message = "사유는 500자를 넘어갈 수 없습니다.") String changeDetailReason)
        implements UpdateOrderV1ApiRequest {
}
