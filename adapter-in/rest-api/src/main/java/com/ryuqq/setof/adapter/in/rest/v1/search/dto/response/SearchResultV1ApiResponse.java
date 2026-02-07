package com.ryuqq.setof.adapter.in.rest.v1.search.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * SearchResultV1ApiResponse - 검색 결과 응답 DTO.
 *
 * <p>레거시 ProductGroupThumbnail 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>검색 API 응답용 상품 썸네일 정보입니다.
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID
 * @param productGroupName 상품그룹명
 * @param brand 브랜드 정보
 * @param productImageUrl 상품 대표 이미지 URL
 * @param price 가격 정보
 * @param insertDate 등록일시
 * @param averageRating 평균 평점
 * @param reviewCount 리뷰 수
 * @param score 검색 점수 (추천순 정렬 기준)
 * @param favorite 찜 여부
 * @param productStatus 상품 상태
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.setof.connectly.module.product.dto.ProductGroupThumbnail
 */
@Schema(description = "검색 결과 응답")
public record SearchResultV1ApiResponse(
        @Schema(description = "상품그룹 ID", example = "12345") long productGroupId,
        @Schema(description = "셀러 ID", example = "100") long sellerId,
        @Schema(description = "상품그룹명", example = "여름 플라워 원피스") String productGroupName,
        @Schema(description = "브랜드 정보") BrandResponse brand,
        @Schema(
                        description = "상품 대표 이미지 URL",
                        example = "https://cdn.example.com/product/12345.jpg")
                String productImageUrl,
        @Schema(description = "가격 정보") PriceResponse price,
        @Schema(description = "등록일시", example = "2026-01-15 10:30:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime insertDate,
        @Schema(description = "평균 평점", example = "4.5") double averageRating,
        @Schema(description = "리뷰 수", example = "128") long reviewCount,
        @Schema(description = "검색 점수 (추천순 정렬 기준)", example = "85.5") double score,
        @Schema(description = "찜 여부", example = "false") boolean favorite,
        @Schema(description = "상품 상태") ProductStatusResponse productStatus) {

    /** BrandResponse - 브랜드 응답 DTO. */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "50") long brandId,
            @Schema(description = "브랜드명", example = "NIKE") String brandName) {}

    /** PriceResponse - 가격 응답 DTO. */
    @Schema(description = "가격 정보")
    public record PriceResponse(
            @Schema(description = "정가", example = "59000") long regularPrice,
            @Schema(description = "현재가", example = "49000") long currentPrice,
            @Schema(description = "판매가", example = "39000") long salePrice,
            @Schema(description = "직접 할인율 (%)", example = "20") int directDiscountRate,
            @Schema(description = "직접 할인 금액", example = "10000") long directDiscountPrice,
            @Schema(description = "총 할인율 (%)", example = "34") int discountRate) {}

    /** ProductStatusResponse - 상품 상태 응답 DTO. */
    @Schema(description = "상품 상태")
    public record ProductStatusResponse(
            @Schema(description = "전시 여부", example = "Y") String displayYn,
            @Schema(description = "품절 여부", example = "N") String soldOutYn) {}
}
