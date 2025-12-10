package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 날짜별 주문 대시보드 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "날짜별 주문 대시보드 응답")
public record OrderDateDashboardV1ApiResponse(
        @Schema(description = "주문 통계") OrderStatisticsV1ApiResponse orderStatistics,
        @Schema(description = "주문 대시보드 랭킹 목록")
                List<OrderDashboardRankingV1ApiResponse> orderDashBoardRankings,
        @Schema(description = "외부몰 주문 통계 목록")
                List<OrderStatisticsV1ApiResponse> externalMallOrderStatistics) {

    @Schema(description = "주문 통계")
    public record OrderStatisticsV1ApiResponse(
            @Schema(description = "플래그", example = "2024-01") String flag,
            @Schema(description = "주문 수", example = "100") Long orderCount,
            @Schema(description = "순수 주문 수", example = "80") Long pureOrderCount,
            @Schema(description = "주문 금액", example = "10000000") java.math.BigDecimal orderAmount,
            @Schema(description = "순수 주문 금액", example = "8000000")
                    java.math.BigDecimal pureOrderAmount) {}

    @Schema(description = "주문 대시보드 랭킹")
    public record OrderDashboardRankingV1ApiResponse(
            @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
            @Schema(description = "상품 그룹명", example = "멋진 티셔츠") String productGroupName,
            @Schema(description = "총 주문 수", example = "100") Long totalOrderCount,
            @Schema(description = "순수 주문 수", example = "80") Long pureOrderCount) {}
}
