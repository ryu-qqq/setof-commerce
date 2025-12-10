package com.ryuqq.setof.adapter.in.rest.v1.order.dto.command;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 주문 수정 Request (Sealed Interface)
 *
 * @author development-team
 * @since 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = NormalOrderV1ApiRequest.class, name = "normalOrder"),
        @JsonSubTypes.Type(value = RefundOrderV1ApiRequest.class, name = "refundOrder"),
        @JsonSubTypes.Type(value = ClaimOrderV1ApiRequest.class, name = "claimOrder")})
@Schema(description = "주문 수정 요청")
public sealed interface UpdateOrderV1ApiRequest
        permits NormalOrderV1ApiRequest, RefundOrderV1ApiRequest, ClaimOrderV1ApiRequest {

    @Schema(description = "결제 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "결제 ID는 필수입니다.")
    Long paymentId();

    @Schema(description = "주문 ID", example = "1")
    Long orderId();

    @Schema(description = "주문 상태", example = "CANCELLED",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "주문 상태는 필수입니다.")
    String orderStatus();

    @Schema(description = "변경 사유", example = "고객 요청")
    String changeReason();

    @Schema(description = "변경 상세 사유", example = "단순 변심")
    String changeDetailReason();
}
