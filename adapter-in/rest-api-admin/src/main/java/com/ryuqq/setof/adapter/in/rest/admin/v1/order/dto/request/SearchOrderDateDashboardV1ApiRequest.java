package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * SearchOrderDateDashboardV1ApiRequest - 기간별 주문 대시보드 검색 요청 DTO.
 *
 * <p>레거시 BaseDateTimeRangeFilter 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "기간별 주문 대시보드 검색 요청")
public record SearchOrderDateDashboardV1ApiRequest(
        @Parameter(description = "조회 시작일", example = "2025-01-01 00:00:00")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime startDate,
        @Parameter(description = "조회 종료일", example = "2025-01-31 23:59:59")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime endDate) {}
