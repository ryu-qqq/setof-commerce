package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * V1 정산 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "정산 응답")
public record SettlementV1ApiResponse(
        @Schema(description = "주문 수", example = "100") Long orderCount,
        @Schema(description = "우리몰 주문 수", example = "80") Long ourMallOrderCount,
        @Schema(description = "외부몰 주문 수", example = "20") Long externalMallOrderCount,
        @Schema(description = "정산일", example = "2024-01-15") String settlementDay,
        @Schema(description = "정산 완료일", example = "2024-01-20") String settlementCompleteDay,
        @Schema(description = "총 현재가", example = "10000000") BigDecimal totalCurrentPrice,
        @Schema(description = "총 할인 금액", example = "1000000") BigDecimal totalDiscountAmount,
        @Schema(description = "셀러 할인 금액", example = "500000") BigDecimal sellerDiscountAmount,
        @Schema(description = "우리 할인 금액", example = "500000") BigDecimal ourDiscountAmount,
        @Schema(description = "총 마일리지 금액", example = "50000") BigDecimal totalMileageAmount,
        @Schema(description = "셀러 마일리지 금액", example = "25000") BigDecimal sellerMileageAmount,
        @Schema(description = "우리 마일리지 금액", example = "25000") BigDecimal ourMileageAmount,
        @Schema(description = "정산 금액", example = "8950000") BigDecimal settlementAmount,
        @Schema(description = "예상 정산 금액",
                example = "8900000") BigDecimal expectationSettlementAmount,
        @Schema(description = "총 수수료", example = "50000") BigDecimal totalFee) {
}
