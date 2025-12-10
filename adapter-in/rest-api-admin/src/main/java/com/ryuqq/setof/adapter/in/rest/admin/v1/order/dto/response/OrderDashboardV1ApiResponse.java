package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 주문 대시보드 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "주문 대시보드 응답")
public record OrderDashboardV1ApiResponse(
        @Schema(description = "오늘 대시보드") TodayDashboardV1ApiResponse todayDashboard,
        @Schema(description = "월별 주문 통계 목록")
                List<OrderStatisticsV1ApiResponse> monthOrderStatistics) {

    @Schema(description = "오늘 대시보드")
    public record TodayDashboardV1ApiResponse(
            @Schema(description = "주문 수", example = "10") Long orderCount,
            @Schema(description = "클레임 수", example = "2") Long claimCount,
            @Schema(description = "주문 문의 수", example = "5") Long orderInquiryCount,
            @Schema(description = "상품 문의 수", example = "3") Long productInquiryCount) {}

    @Schema(description = "주문 통계")
    public record OrderStatisticsV1ApiResponse(
            @Schema(description = "플래그", example = "2024-01") String flag,
            @Schema(description = "주문 수", example = "100") Long orderCount,
            @Schema(description = "순수 주문 수", example = "80") Long pureOrderCount,
            @Schema(description = "주문 금액", example = "10000000") java.math.BigDecimal orderAmount,
            @Schema(description = "순수 주문 금액", example = "8000000")
                    java.math.BigDecimal pureOrderAmount) {}
}
