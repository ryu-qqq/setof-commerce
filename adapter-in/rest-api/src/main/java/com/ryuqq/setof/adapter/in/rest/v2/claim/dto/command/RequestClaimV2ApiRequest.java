package com.ryuqq.setof.adapter.in.rest.v2.claim.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * RequestClaimV2ApiRequest - 클레임 요청 API Request DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "클레임 요청")
public record RequestClaimV2ApiRequest(
        @Schema(
                        description = "주문 ID",
                        example = "550e8400-e29b-41d4-a716-446655440000",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "주문 ID는 필수입니다")
                String orderId,
        @Schema(description = "주문 상품 ID (전체 주문 클레임 시 null)", example = "item-001")
                String orderItemId,
        @Schema(
                        description = "클레임 유형",
                        example = "CANCEL",
                        requiredMode = Schema.RequiredMode.REQUIRED,
                        allowableValues = {"CANCEL", "RETURN", "EXCHANGE", "PARTIAL_REFUND"})
                @NotBlank(message = "클레임 유형은 필수입니다")
                String claimType,
        @Schema(
                        description = "클레임 사유",
                        example = "SIMPLE_CHANGE_OF_MIND",
                        requiredMode = Schema.RequiredMode.REQUIRED,
                        allowableValues = {
                            "SIMPLE_CHANGE_OF_MIND",
                            "WRONG_SIZE",
                            "WRONG_COLOR",
                            "FOUND_CHEAPER",
                            "DELIVERY_DELAY",
                            "NOT_AS_DESCRIBED",
                            "DEFECTIVE_PRODUCT",
                            "MISSING_PARTS",
                            "WRONG_ITEM_DELIVERED",
                            "DAMAGED_DURING_DELIVERY",
                            "OTHER"
                        })
                @NotBlank(message = "클레임 사유는 필수입니다")
                String claimReason,
        @Schema(description = "상세 사유", example = "사이즈가 맞지 않습니다") String claimReasonDetail,
        @Schema(description = "클레임 수량", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "수량은 필수입니다")
                @Min(value = 1, message = "수량은 1 이상이어야 합니다")
                Integer quantity,
        @Schema(
                        description = "환불 예정 금액",
                        example = "35000",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "환불 금액은 필수입니다")
                @DecimalMin(value = "0", message = "환불 금액은 0 이상이어야 합니다")
                BigDecimal refundAmount) {}
