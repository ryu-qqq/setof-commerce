package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

/**
 * CartV1ApiResponse - 장바구니 아이템 응답 DTO.
 *
 * <p>레거시 CartResponse 기반 flat 구조 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param productGroupName 상품그룹명
 * @param sellerId 판매자 ID
 * @param sellerName 판매자명
 * @param productId 상품(SKU) ID
 * @param price 가격 정보
 * @param quantity 장바구니 수량
 * @param stockQuantity 현재 재고 수량
 * @param optionValue 옵션 값 (예: "블랙 270")
 * @param cartId 장바구니 ID
 * @param productGroupId 상품그룹 ID
 * @param imageUrl 대표 이미지 URL
 * @param productStatus 상품 상태 (ON_SALE, SOLD_OUT 등)
 * @param categories 카테고리 목록
 * @param mileage 마일리지 정보
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.cart.dto.CartResponse
 */
@Schema(description = "장바구니 아이템 응답")
public record CartV1ApiResponse(
        @Schema(description = "브랜드 ID", example = "4534") long brandId,
        @Schema(description = "브랜드명", example = "Humanmade") String brandName,
        @Schema(description = "상품그룹명", example = "에어맥스 90") String productGroupName,
        @Schema(description = "판매자 ID", example = "22") long sellerId,
        @Schema(description = "판매자명", example = "THEGRANDE") String sellerName,
        @Schema(description = "상품(SKU) ID", example = "717278") long productId,
        @Schema(description = "가격 정보") PriceResponse price,
        @Schema(description = "장바구니 수량", example = "2") int quantity,
        @Schema(description = "현재 재고 수량", example = "1") int stockQuantity,
        @Schema(description = "옵션 값 (예: 블랙 270)", example = "M") String optionValue,
        @Schema(description = "장바구니 ID", example = "1840") long cartId,
        @Schema(description = "상품그룹 ID", example = "88403") long productGroupId,
        @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/image.jpg")
                String imageUrl,
        @Schema(description = "상품 상태", example = "ON_SALE") String productStatus,
        @Schema(description = "카테고리 목록") Set<CategoryResponse> categories,
        @Schema(description = "마일리지 정보") MileageResponse mileage) {

    /**
     * PriceResponse - 가격 정보 응답.
     *
     * @param regularPrice 정가
     * @param currentPrice 현재 판매가
     * @param salePrice 할인가
     * @param directDiscountRate 직접 할인율
     * @param directDiscountPrice 직접 할인가
     * @param discountRate 전체 할인율
     */
    @Schema(description = "가격 정보")
    public record PriceResponse(
            @Schema(description = "정가", example = "1280000") int regularPrice,
            @Schema(description = "현재 판매가", example = "1030000") int currentPrice,
            @Schema(description = "할인가", example = "957900") int salePrice,
            @Schema(description = "직접 할인율", example = "7") int directDiscountRate,
            @Schema(description = "직접 할인가", example = "72100") int directDiscountPrice,
            @Schema(description = "전체 할인율", example = "25") int discountRate) {}

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
