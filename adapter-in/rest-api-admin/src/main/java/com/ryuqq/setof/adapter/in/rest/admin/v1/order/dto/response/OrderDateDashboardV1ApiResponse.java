package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

/**
 * OrderDateDashboardV1ApiResponse - 기간별 주문 대시보드 응답 DTO.
 *
 * <p>레거시 OrderDateDashboardResponse 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "기간별 주문 대시보드 응답")
public record OrderDateDashboardV1ApiResponse(
        @Schema(description = "기간 전체 통계") OrderStatisticsResponse orderStatistics,
        @Schema(description = "상품별 주문 랭킹 (Top 10)")
                List<OrderDashboardRankingResponse> orderDashBoardRankings,
        @Schema(description = "외부몰별 주문 통계")
                List<OrderStatisticsResponse> externalMallOrderStatistics) {

    @Schema(description = "주문 통계")
    public record OrderStatisticsResponse(
            @Schema(description = "구분 (Total 또는 채널명)", example = "Total") String flag,
            @Schema(description = "총 주문 건수", example = "5000") long orderCount,
            @Schema(description = "순수 주문 건수 (취소/반품 제외)", example = "4500") long pureOrderCount,
            @Schema(description = "총 주문 금액", example = "250000000") BigDecimal orderAmount,
            @Schema(description = "순수 주문 금액", example = "225000000") BigDecimal pureOrderAmount) {}

    @Schema(description = "상품별 주문 랭킹")
    public record OrderDashboardRankingResponse(
            @Schema(description = "상품그룹 ID", example = "101") long productGroupId,
            @Schema(description = "상품그룹명", example = "인기 상품 A") String productGroupName,
            @Schema(description = "총 주문 건수", example = "500") long totalOrderCount,
            @Schema(description = "순수 주문 건수", example = "480") long pureOrderCount) {}
}
