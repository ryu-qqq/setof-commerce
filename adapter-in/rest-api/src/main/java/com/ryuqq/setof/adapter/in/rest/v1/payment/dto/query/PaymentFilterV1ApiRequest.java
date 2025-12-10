package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

/**
 * V1 결제 목록 조회 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "결제 목록 조회 필터")
public record PaymentFilterV1ApiRequest(
        @Schema(description = "마지막 조회한 도메인 ID (커서 기반 페이징)", example = "100") Long lastDomainId,
        @Schema(description = "주문 상태 목록", example = "[\"COMPLETED\", \"SHIPPING\"]")
                List<String> orderStatusList,
        @Schema(description = "시작 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime startDate,
        @Schema(description = "종료 일시", example = "2024-12-31 23:59:59")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime endDate,
        @Schema(description = "페이지 크기", example = "20") @Min(1) @Max(100) Integer pageSize) {}
