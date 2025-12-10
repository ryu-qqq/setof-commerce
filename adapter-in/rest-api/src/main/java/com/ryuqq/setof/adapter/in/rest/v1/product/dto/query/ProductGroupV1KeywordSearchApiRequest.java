package com.ryuqq.setof.adapter.in.rest.v1.product.dto.query;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 상품 키워드 검색 조회 필터 Request
 *
 * <p>
 * 상품을 키워드로 검색하여 목록을 조회할 때 사용하는 필터 조건입니다. 기존 상품 목록 조회 필터에 검색 키워드와 상품 그룹 ID 필터를 추가로 지원합니다.
 *
 * @param searchWord 검색 키워드
 * @param productGroupId 상품 그룹 ID
 * @param lastDomainId 마지막 조회한 도메인 ID (커서 기반 페이징)
 * @param cursorValue 커서 값 (정렬 기준 값)
 * @param lowestPrice 최저 가격
 * @param highestPrice 최고 가격
 * @param categoryId 카테고리 ID
 * @param categoryIds 카테고리 ID 목록
 * @param brandId 브랜드 ID
 * @param brandIds 브랜드 ID 목록
 * @param sellerId 판매자 ID
 * @param orderType 정렬 타입
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 키워드 검색 조회 필터")
public record ProductGroupV1KeywordSearchApiRequest(
        @Schema(description = "검색 키워드", example = "티셔츠") String searchWord,
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "마지막 조회한 도메인 ID (커서 기반 페이징)", example = "100") Long lastDomainId,
        @Schema(description = "커서 값 (정렬 기준 값)", example = "2024-01-01T00:00:00") String cursorValue,
        @Schema(description = "최저 가격", example = "10000") Long lowestPrice,
        @Schema(description = "최고 가격", example = "100000") Long highestPrice,
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "카테고리 ID 목록", example = "[1, 2, 3]") List<Long> categoryIds,
        @Schema(description = "브랜드 ID", example = "1") Long brandId,
        @Schema(description = "브랜드 ID 목록", example = "[1, 2, 3]") List<Long> brandIds,
        @Schema(description = "판매자 ID", example = "100") Long sellerId,
        @Schema(description = "정렬 타입", example = "LATEST") String orderType) {
}
