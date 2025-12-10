package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * V1 상품 그룹 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 필터")
public record ProductGroupFilterV1ApiRequest(
        @Schema(description = "마지막 조회한 도메인 ID (커서 기반 페이징)", example = "100") Long lastDomainId,
        @Schema(description = "관리 타입", example = "SELF") String managementType,
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "브랜드 ID", example = "1") Long brandId,
        @Schema(description = "품절 여부 (Y/N)", example = "N") String soldOutYn,
        @Schema(description = "전시 여부 (Y/N)", example = "Y") String displayYn,
        @Schema(description = "최소 판매가", example = "10000") Long minSalePrice,
        @Schema(description = "최대 판매가", example = "100000") Long maxSalePrice,
        @Schema(description = "최소 할인율", example = "10") Long minDiscountRate,
        @Schema(description = "최대 할인율", example = "50") Long maxDiscountRate,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "카테고리 ID 목록", example = "[1, 2, 3]") Set<Long> categoryIds,
        @Schema(description = "검색어", example = "상품명") String searchKeyword,
        @Schema(description = "시작 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
        @Schema(description = "종료 일시", example = "2024-12-31 23:59:59") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
}
