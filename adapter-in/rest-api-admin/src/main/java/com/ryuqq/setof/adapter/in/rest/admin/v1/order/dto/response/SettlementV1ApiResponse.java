package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * SettlementV1ApiResponse - 정산 응답 DTO.
 *
 * <p>레거시 SettlementResponse 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "정산 응답")
public record SettlementV1ApiResponse(
        @Schema(description = "총 주문 건수", example = "150") long orderCount,
        @Schema(description = "자사몰 주문 건수", example = "100") long ourMallOrderCount,
        @Schema(description = "외부몰 주문 건수", example = "50") long externalMallOrderCount,
        @Schema(description = "정산 예정일", example = "2025-01-20") String settlementDay,
        @Schema(description = "정산 완료일", example = "2025-01-25") String settlementCompleteDay,
        @Schema(description = "총 정가 금액", example = "15000000") BigDecimal totalCurrentPrice,
        @Schema(description = "총 할인 금액", example = "500000") BigDecimal totalDiscountAmount,
        @Schema(description = "셀러 부담 할인 금액", example = "250000") BigDecimal sellerDiscountAmount,
        @Schema(description = "자사 부담 할인 금액", example = "250000") BigDecimal ourDiscountAmount,
        @Schema(description = "총 마일리지 사용 금액", example = "100000") BigDecimal totalMileageAmount,
        @Schema(description = "셀러 부담 마일리지", example = "50000") BigDecimal sellerMileageAmount,
        @Schema(description = "자사 부담 마일리지", example = "50000") BigDecimal ourMileageAmount,
        @Schema(description = "정산 금액", example = "14400000") BigDecimal settlementAmount,
        @Schema(description = "예상 정산 금액 (수수료 차감 후)", example = "13680000")
                BigDecimal expectationSettlementAmount,
        @Schema(description = "총 수수료", example = "720000") BigDecimal totalFee) {}
