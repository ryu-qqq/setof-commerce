package com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * WishlistItemV1ApiResponse - 찜 항목 응답 DTO.
 *
 * <p>레거시 UserFavoriteThumbnail 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-003: Response는 @Schema 어노테이션.
 *
 * <p>레거시 응답 구조:
 *
 * <pre>
 * {
 *   "userFavoriteId": 1,
 *   "productGroupId": 1001,
 *   "sellerId": 5,
 *   "productGroupName": "상품명",
 *   "brand": { "brandId": 3, "brandName": "브랜드명" },
 *   "imageUrl": "https://...",
 *   "price": { "regularPrice": 50000, "currentPrice": 40000, "discountRate": 20 },
 *   "insertDate": "2024-01-01T10:30:00",
 *   "productStatus": { "soldOut": false, "displayed": true }
 * }
 * </pre>
 *
 * @param userFavoriteId 찜 ID
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 셀러 ID
 * @param productGroupName 상품 그룹명
 * @param brand 브랜드 정보
 * @param imageUrl 상품 이미지 URL
 * @param price 가격 정보
 * @param insertDate 찜 등록일 (ISO 8601)
 * @param productStatus 상품 상태 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "찜 항목 응답")
public record WishlistItemV1ApiResponse(
        @Schema(description = "찜 ID", example = "1") long userFavoriteId,
        @Schema(description = "상품 그룹 ID", example = "1001") long productGroupId,
        @Schema(description = "셀러 ID", example = "5") long sellerId,
        @Schema(description = "상품 그룹명", example = "나이키 에어맥스 90") String productGroupName,
        @Schema(description = "브랜드 정보") BrandResponse brand,
        @Schema(description = "상품 이미지 URL", example = "https://example.com/image.jpg")
                String imageUrl,
        @Schema(description = "가격 정보") PriceResponse price,
        @Schema(description = "찜 등록일 (ISO 8601)", example = "2024-01-01T10:30:00")
                String insertDate,
        @Schema(description = "상품 상태 정보") ProductStatusResponse productStatus) {

    /** BrandResponse - 브랜드 정보 응답 DTO. */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "3") long brandId,
            @Schema(description = "브랜드명", example = "Nike") String brandName) {}

    /** PriceResponse - 가격 정보 응답 DTO. */
    @Schema(description = "가격 정보")
    public record PriceResponse(
            @Schema(description = "정상가", example = "50000") BigDecimal regularPrice,
            @Schema(description = "판매가", example = "40000") BigDecimal currentPrice,
            @Schema(description = "할인율 (%)", example = "20") int discountRate) {}

    /** ProductStatusResponse - 상품 상태 응답 DTO. */
    @Schema(description = "상품 상태 정보")
    public record ProductStatusResponse(
            @Schema(description = "품절 여부", example = "false") boolean soldOut,
            @Schema(description = "전시 여부", example = "true") boolean displayed) {}
}
