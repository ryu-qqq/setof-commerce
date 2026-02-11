package com.ryuqq.setof.adapter.in.rest.v1.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * UserFavoriteV1ApiResponse - 찜 상품 응답 DTO.
 *
 * <p>레거시 UserFavoriteThumbnail, ProductGroupThumbnail 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param userFavoriteId 찜 ID
 * @param productGroupId 상품그룹 ID
 * @param sellerId 판매자 ID
 * @param productGroupName 상품그룹명
 * @param brand 브랜드 정보
 * @param productImageUrl 대표 이미지 URL
 * @param price 가격 정보
 * @param insertDate 등록일시
 * @param productStatus 상품 상태 정보
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.user.dto.favorite.UserFavoriteThumbnail
 */
@Schema(description = "찜 상품 응답")
public record UserFavoriteV1ApiResponse(
        @Schema(description = "찜 ID", example = "100") long userFavoriteId,
        @Schema(description = "상품그룹 ID", example = "1234") long productGroupId,
        @Schema(description = "판매자 ID", example = "10") long sellerId,
        @Schema(description = "상품그룹명", example = "프리미엄 티셔츠") String productGroupName,
        @Schema(description = "브랜드 정보") BrandResponse brand,
        @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/images/product.jpg")
                String productImageUrl,
        @Schema(description = "가격 정보") PriceResponse price,
        @Schema(description = "등록일시", example = "2024-01-20T15:30:00") LocalDateTime insertDate,
        @Schema(description = "상품 상태 정보") ProductStatusResponse productStatus) {

    /**
     * BrandResponse - 브랜드 정보 응답.
     *
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명
     */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "5") long brandId,
            @Schema(description = "브랜드명", example = "NIKE") String brandName) {}

    /**
     * PriceResponse - 가격 정보 응답.
     *
     * @param regularPrice 정가
     * @param currentPrice 현재 판매가
     * @param discountRate 할인율 (%)
     */
    @Schema(description = "가격 정보")
    public record PriceResponse(
            @Schema(description = "정가", example = "50000") long regularPrice,
            @Schema(description = "현재 판매가", example = "35000") long currentPrice,
            @Schema(description = "할인율 (%)", example = "30") int discountRate) {}

    /**
     * ProductStatusResponse - 상품 상태 정보 응답.
     *
     * @param soldOut 품절 여부
     * @param display 전시 여부
     */
    @Schema(description = "상품 상태 정보")
    public record ProductStatusResponse(
            @Schema(description = "품절 여부", example = "false") boolean soldOut,
            @Schema(description = "전시 여부", example = "true") boolean display) {}
}
