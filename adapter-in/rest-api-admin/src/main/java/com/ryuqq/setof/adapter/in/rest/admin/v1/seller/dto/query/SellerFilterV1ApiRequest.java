package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 셀러 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "셀러 필터")
public record SellerFilterV1ApiRequest(
        @Schema(description = "검색 키워드 타입", example = "SELLER_NAME") String searchKeyword,
        @Schema(description = "검색어", example = "셀러명") String searchWord,
        @Schema(description = "사이트 ID 목록", example = "[1, 2, 3]") List<Long> siteIds,
        @Schema(description = "승인 상태", example = "APPROVED") String status) {}
