package com.ryuqq.setof.adapter.in.rest.v1.content.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * ComponentProductV1ApiResponse - 컴포넌트 상품 응답 DTO.
 *
 * <p>레거시 ProductGroupThumbnail 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "컴포넌트 상품 응답")
public record ComponentProductV1ApiResponse(
        @Schema(description = "상품그룹 ID", example = "123") long productGroupId,
        @Schema(description = "셀러 ID", example = "10") long sellerId,
        @Schema(description = "상품그룹명", example = "여름 원피스") String productGroupName,
        @Schema(description = "브랜드 정보") BrandResponse brand,
        @Schema(description = "상품 이미지 URL", example = "https://cdn.example.com/product/123.jpg")
                String productImageUrl,
        @Schema(description = "가격 정보") PriceResponse price,
        @Schema(description = "등록일", example = "2026-01-15 10:30:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime insertDate,
        @Schema(description = "평균 평점", example = "4.5") double averageRating,
        @Schema(description = "리뷰 수", example = "128") long reviewCount,
        @Schema(description = "검색 점수", example = "85.5") double score,
        @Schema(description = "찜 여부", example = "false") boolean favorite,
        @Schema(description = "상품 상태") ProductStatusResponse productStatus) {

    /** BrandResponse - 브랜드 응답 DTO. */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "5") long brandId,
            @Schema(description = "브랜드명", example = "Fashion Brand") String brandName) {}

    /** PriceResponse - 가격 응답 DTO. */
    @Schema(description = "가격 정보")
    public record PriceResponse(
            @Schema(description = "정가", example = "59000") long regularPrice,
            @Schema(description = "판매가", example = "39000") long salePrice,
            @Schema(description = "할인율 (%)", example = "33") int discountRate) {}

    /** ProductStatusResponse - 상품 상태 응답 DTO. */
    @Schema(description = "상품 상태")
    public record ProductStatusResponse(
            @Schema(description = "전시 여부", example = "Y") String displayYn,
            @Schema(description = "품절 여부", example = "N") String soldOutYn) {}
}
