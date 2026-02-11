package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

/**
 * CartV1ApiResponse - 장바구니 아이템 응답 DTO.
 *
 * <p>레거시 CartResponse 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param cartId 장바구니 ID
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param productId 상품(SKU) ID
 * @param quantity 장바구니 수량
 * @param stockQuantity 현재 재고 수량
 * @param optionValue 옵션 값 (예: "블랙 270")
 * @param imageUrl 대표 이미지 URL
 * @param productStatus 상품 상태 (ON_SALE, SOLD_OUT 등)
 * @param brand 브랜드 정보
 * @param seller 판매자 정보
 * @param price 가격 정보
 * @param mileage 마일리지 정보
 * @param categories 카테고리 목록
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.cart.dto.CartResponse
 */
@Schema(description = "장바구니 아이템 응답")
public record CartV1ApiResponse(
        @Schema(description = "장바구니 ID", example = "1001") long cartId,
        @Schema(description = "상품그룹 ID", example = "100") long productGroupId,
        @Schema(description = "상품그룹명", example = "에어맥스 90") String productGroupName,
        @Schema(description = "상품(SKU) ID", example = "500") long productId,
        @Schema(description = "장바구니 수량", example = "2") int quantity,
        @Schema(description = "현재 재고 수량", example = "50") int stockQuantity,
        @Schema(description = "옵션 값 (예: 블랙 270)", example = "블랙 270") String optionValue,
        @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/image.jpg")
                String imageUrl,
        @Schema(description = "상품 상태", example = "ON_SALE") String productStatus,
        @Schema(description = "브랜드 정보") BrandResponse brand,
        @Schema(description = "판매자 정보") SellerResponse seller,
        @Schema(description = "가격 정보") PriceResponse price,
        @Schema(description = "마일리지 정보") MileageResponse mileage,
        @Schema(description = "카테고리 목록") Set<CategoryResponse> categories) {

    /**
     * BrandResponse - 브랜드 정보 응답.
     *
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명
     */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "10") long brandId,
            @Schema(description = "브랜드명", example = "나이키") String brandName) {}

    /**
     * SellerResponse - 판매자 정보 응답.
     *
     * @param sellerId 판매자 ID
     * @param sellerName 판매자명
     */
    @Schema(description = "판매자 정보")
    public record SellerResponse(
            @Schema(description = "판매자 ID", example = "5") long sellerId,
            @Schema(description = "판매자명", example = "공식스토어") String sellerName) {}

    /**
     * PriceResponse - 가격 정보 응답.
     *
     * @param regularPrice 정가
     * @param currentPrice 현재 판매가
     * @param salePrice 할인가
     */
    @Schema(description = "가격 정보")
    public record PriceResponse(
            @Schema(description = "정가", example = "150000") int regularPrice,
            @Schema(description = "현재 판매가", example = "129000") int currentPrice,
            @Schema(description = "할인가", example = "129000") int salePrice) {}

    /**
     * MileageResponse - 마일리지 정보 응답.
     *
     * @param mileageRate 마일리지 적립률 (0.01 = 1%)
     * @param expectedMileageAmount 예상 적립 마일리지
     */
    @Schema(description = "마일리지 정보")
    public record MileageResponse(
            @Schema(description = "마일리지 적립률 (0.01 = 1%)", example = "0.01") double mileageRate,
            @Schema(description = "예상 적립 마일리지", example = "1290") double expectedMileageAmount) {}

    /**
     * CategoryResponse - 카테고리 정보 응답.
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     */
    @Schema(description = "카테고리 정보")
    public record CategoryResponse(
            @Schema(description = "카테고리 ID", example = "10") long categoryId,
            @Schema(description = "카테고리명", example = "운동화") String categoryName) {}
}
