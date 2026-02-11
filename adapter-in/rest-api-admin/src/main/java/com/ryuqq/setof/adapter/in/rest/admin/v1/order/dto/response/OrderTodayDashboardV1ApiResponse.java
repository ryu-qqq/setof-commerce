package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

/**
 * OrderTodayDashboardV1ApiResponse - 오늘 주문 대시보드 응답 DTO.
 *
 * <p>레거시 OrderDashboardResponse 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "오늘 주문 대시보드 응답")
public record OrderTodayDashboardV1ApiResponse(
        @Schema(description = "오늘 현황") TodayDashboardResponse todayDashboard,
        @Schema(description = "월별 주문 통계") List<OrderStatisticsResponse> monthOrderStatistics) {

    @Schema(description = "오늘 현황")
    public record TodayDashboardResponse(
            @Schema(description = "오늘 주문 건수", example = "25") long orderCount,
            @Schema(description = "오늘 클레임 건수", example = "3") long claimCount,
            @Schema(description = "주문 문의 건수", example = "8") long orderInquiryCount,
            @Schema(description = "상품 문의 건수", example = "12") long productInquiryCount) {}

    @Schema(description = "주문 통계")
    public record OrderStatisticsResponse(
            @Schema(description = "구분 (월 또는 채널명)", example = "2025-01") String flag,
            @Schema(description = "총 주문 건수", example = "1500") long orderCount,
            @Schema(description = "순수 주문 건수 (취소/반품 제외)", example = "1350") long pureOrderCount,
            @Schema(description = "총 주문 금액", example = "75000000") BigDecimal orderAmount,
            @Schema(description = "순수 주문 금액", example = "67500000") BigDecimal pureOrderAmount) {}
}
