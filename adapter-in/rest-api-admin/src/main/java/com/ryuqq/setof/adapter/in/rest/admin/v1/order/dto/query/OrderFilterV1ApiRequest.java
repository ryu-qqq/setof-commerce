package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * V1 주문 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "주문 필터")
public record OrderFilterV1ApiRequest(
        @Schema(description = "마지막 조회한 도메인 ID (커서 기반 페이징)", example = "100") Long lastDomainId,
        @Schema(description = "주문 상태 목록", example = "[\"COMPLETED\", \"SHIPPING\"]")
                List<String> orderStatusList,
        @Schema(
                        description = "기간 타입",
                        example = "MONTH",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String periodType,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "검색어", example = "주문") String searchKeyword,
        @Schema(description = "시작 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime startDate,
        @Schema(description = "종료 일시", example = "2024-12-31 23:59:59")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime endDate) {}
