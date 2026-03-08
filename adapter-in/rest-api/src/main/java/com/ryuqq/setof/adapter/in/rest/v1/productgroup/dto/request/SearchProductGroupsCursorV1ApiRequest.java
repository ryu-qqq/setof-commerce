package com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * SearchProductGroupsCursorV1ApiRequest - 상품그룹 목록 검색 요청 DTO (커서 페이징).
 *
 * <p>레거시 ProductFilter (AbstractItemFilter 상속) 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-007: @Parameter 어노테이션 (Query Parameters / @ModelAttribute).
 *
 * <p>API-DTO-010: 커서 페이징은 Search{Domain}CursorV1ApiRequest 네이밍.
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param lastDomainId 커서 페이징 기준 ID (이전 페이지 마지막 productGroupId)
 * @param cursorValue 커서 값 (정렬 기준 값: 날짜/가격/할인율 등)
 * @param lowestPrice 최저 판매가 필터
 * @param highestPrice 최고 판매가 필터
 * @param categoryId 단일 카테고리 ID 필터
 * @param brandId 단일 브랜드 ID 필터
 * @param sellerId 판매자 ID 필터
 * @param categoryIds 다중 카테고리 ID 필터
 * @param brandIds 다중 브랜드 ID 필터
 * @param orderType 정렬 기준
 * @param size 페이지 크기 (1~100)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.product.dto.filter.ProductFilter
 */
@Schema(description = "상품그룹 목록 검색 요청 (커서 페이징)")
public record SearchProductGroupsCursorV1ApiRequest(
        @Parameter(description = "커서 기준 마지막 상품그룹 ID (다음 페이지 조회 시 사용)", example = "980")
                @Schema(description = "커서: 마지막 상품그룹 ID", nullable = true)
                Long lastDomainId,
        @Parameter(description = "커서 값 (정렬 기준 값: 점수/날짜/가격/할인율 등)", example = "0.85")
                @Schema(description = "커서: 정렬 기준 값", nullable = true)
                String cursorValue,
        @Parameter(description = "최저 판매가 필터", example = "10000") Long lowestPrice,
        @Parameter(description = "최고 판매가 필터", example = "100000") Long highestPrice,
        @Parameter(description = "단일 카테고리 ID 필터", example = "10") Long categoryId,
        @Parameter(description = "단일 브랜드 ID 필터", example = "5") Long brandId,
        @Parameter(description = "판매자 ID 필터", example = "1") Long sellerId,
        @Parameter(description = "다중 카테고리 ID 필터 (최대 100개)")
                @Size(max = 100, message = "카테고리 ID는 최대 100개까지 지정할 수 있습니다.")
                List<Long> categoryIds,
        @Parameter(description = "다중 브랜드 ID 필터 (최대 100개)")
                @Size(max = 100, message = "브랜드 ID는 최대 100개까지 지정할 수 있습니다.")
                List<Long> brandIds,
        @Parameter(
                        description = "정렬 기준",
                        example = "RECOMMEND",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "NONE",
                                            "RECOMMEND",
                                            "REVIEW",
                                            "RECENT",
                                            "HIGH_RATING",
                                            "LOW_PRICE",
                                            "HIGH_PRICE",
                                            "LOW_DISCOUNT",
                                            "HIGH_DISCOUNT"
                                        },
                                        nullable = true))
                String orderType,
        @Parameter(description = "조회할 아이템 수 (1~100)", example = "20")
                @Min(value = 1, message = "조회 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "조회 크기는 100 이하여야 합니다.")
                Integer size) {}
