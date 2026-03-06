package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.query;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * SearchSellersApiRequest - 셀러 목록 조회 API Request.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-003: Validation 어노테이션은 API Request에만 적용.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*ApiRequest).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 목록 조회 요청")
public record SearchSellersApiRequest(
        @Parameter(description = "활성화 여부 (null이면 전체 조회)")
                @Schema(description = "활성화 여부", nullable = true)
                Boolean active,
        @Parameter(description = "검색 필드") @Schema(description = "검색 필드", nullable = true)
                String searchField,
        @Parameter(description = "검색어") @Schema(description = "검색어", nullable = true)
                String searchWord,
        @Parameter(description = "정렬 키 (CREATED_AT, SELLER_NAME)")
                @Schema(description = "정렬 키", nullable = true)
                String sortKey,
        @Parameter(description = "정렬 방향 (ASC, DESC)")
                @Schema(description = "정렬 방향", nullable = true)
                String sortDirection,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Schema(description = "페이지 번호", minimum = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
                Integer page,
        @Parameter(description = "페이지 크기", example = "20")
                @Schema(description = "페이지 크기", minimum = "1", maximum = "100")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
                Integer size) {}
