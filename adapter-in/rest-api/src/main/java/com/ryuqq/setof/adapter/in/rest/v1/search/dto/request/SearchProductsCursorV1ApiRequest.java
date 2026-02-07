package com.ryuqq.setof.adapter.in.rest.v1.search.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

/**
 * SearchProductsCursorV1ApiRequest - 상품 검색 요청 DTO (커서 페이징).
 *
 * <p>레거시 SearchFilter 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*CursorApiRequest - 커서 페이징).
 *
 * @param searchWord 검색어 (MySQL ngram FULLTEXT 검색)
 * @param productGroupId 특정 상품그룹 ID 필터
 * @param lastProductGroupId 마지막으로 조회한 상품그룹 ID (커서 페이징용)
 * @param cursorValue 커서 값 (정렬 기준값, orderType에 따라 다름)
 * @param lowestPrice 최저가 필터
 * @param highestPrice 최고가 필터
 * @param categoryId 카테고리 ID 필터
 * @param brandId 브랜드 ID 필터
 * @param sellerId 셀러 ID 필터
 * @param categoryIds 다중 카테고리 ID 필터
 * @param brandIds 다중 브랜드 ID 필터
 * @param orderType 정렬 타입 (기본값: RECOMMEND)
 * @param size 조회할 아이템 수 (1~100)
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.setof.connectly.module.search.dto.SearchFilter
 */
@Schema(description = "상품 검색 요청 (커서 페이징)")
public record SearchProductsCursorV1ApiRequest(
        @Parameter(description = "검색어 (상품명 FULLTEXT 검색)", example = "원피스")
                @Schema(description = "검색어", nullable = true)
                String searchWord,
        @Parameter(description = "특정 상품그룹 ID 필터", example = "12345")
                @Schema(description = "상품그룹 ID", nullable = true)
                Long productGroupId,
        @Parameter(description = "마지막으로 조회한 상품그룹 ID (다음 페이지 조회 시 사용)", example = "1000")
                @Schema(description = "커서: 마지막 상품그룹 ID", nullable = true)
                Long lastProductGroupId,
        @Parameter(description = "커서 값 (정렬 기준값, orderType에 따라 다름)", example = "85.5")
                @Schema(description = "커서 값", nullable = true)
                String cursorValue,
        @Parameter(description = "최저가 필터", example = "10000")
                @Schema(description = "최저가", nullable = true)
                Long lowestPrice,
        @Parameter(description = "최고가 필터", example = "100000")
                @Schema(description = "최고가", nullable = true)
                Long highestPrice,
        @Parameter(description = "카테고리 ID 필터", example = "1")
                @Schema(description = "카테고리 ID", nullable = true)
                Long categoryId,
        @Parameter(description = "브랜드 ID 필터", example = "1")
                @Schema(description = "브랜드 ID", nullable = true)
                Long brandId,
        @Parameter(description = "셀러 ID 필터", example = "1")
                @Schema(description = "셀러 ID", nullable = true)
                Long sellerId,
        @Parameter(description = "다중 카테고리 ID 필터", example = "[1, 2, 3]")
                @Schema(description = "카테고리 ID 목록", nullable = true)
                List<Long> categoryIds,
        @Parameter(description = "다중 브랜드 ID 필터", example = "[1, 2, 3]")
                @Schema(description = "브랜드 ID 목록", nullable = true)
                List<Long> brandIds,
        @Parameter(
                        description = "정렬 타입 (기본값: RECOMMEND)",
                        example = "RECOMMEND",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "RECOMMEND", "REVIEW", "RECENT", "HIGH_RATING",
                                            "LOW_PRICE", "HIGH_PRICE", "LOW_DISCOUNT",
                                                    "HIGH_DISCOUNT"
                                        }))
                String orderType,
        @Parameter(description = "조회할 아이템 수 (1~100)", example = "20")
                @Schema(description = "페이지 크기", defaultValue = "20")
                @Min(value = 1, message = "조회 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "조회 크기는 100 이하여야 합니다.")
                Integer size) {

    public SearchProductsCursorV1ApiRequest {
        if (size == null) size = 20;
        if (orderType == null) orderType = "RECOMMEND";
        if (categoryIds == null) categoryIds = List.of();
        if (brandIds == null) brandIds = List.of();
    }
}
