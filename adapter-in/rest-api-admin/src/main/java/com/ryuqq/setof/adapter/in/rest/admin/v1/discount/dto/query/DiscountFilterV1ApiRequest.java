package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 할인 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "할인 필터")
public record DiscountFilterV1ApiRequest(
        @Schema(description = "할인 정책 ID", example = "1") Long discountPolicyId,
        @Schema(description = "기간 타입", example = "MONTH") String periodType,
        @Schema(description = "활성 여부 (Y/N)", example = "Y") String activeYn,
        @Schema(description = "발행자 타입", example = "ADMIN") String publisherType,
        @Schema(description = "발행 타입", example = "PRODUCT") String issueType,
        @Schema(description = "사용자 ID", example = "1") Long userId,
        @Schema(description = "검색어", example = "할인") String searchKeyword,
        @Schema(description = "시작 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
        @Schema(description = "종료 일시", example = "2024-12-31 23:59:59") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
}
