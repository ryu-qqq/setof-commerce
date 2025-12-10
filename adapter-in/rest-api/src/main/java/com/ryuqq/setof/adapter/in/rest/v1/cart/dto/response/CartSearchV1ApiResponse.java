package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 장바구니 Response
 *
 * <p>장바구니 단건 정보를 반환하는 응답 DTO입니다.
 *
 * @param cartId 장바구니 ID
 * @param productGroupId 상품 그룹 ID
 * @param productGroupName 상품 그룹명
 * @param productId 상품 ID
 * @param productName 상품명
 * @param optionValue 옵션명
 * @param thumbnailUrl 썸네일 이미지 URL
 * @param sellerId 판매자 ID
 * @param sellerName 판매자명
 * @param price 가격
 * @param discountPrice 할인가
 * @param quantity 수량
 * @param isSoldOut 품절 여부
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "장바구니 응답")
public record CartSearchV1ApiResponse(
        @Schema(description = "브랜드 ID", example = "1") Long brandId,
        @Schema(description = "브랜드 명", example = "1") String brandName,
        @Schema(description = "상품 그룹명", example = "베이직 티셔츠") String productGroupName,
        @Schema(description = "판매자 ID", example = "100") Long sellerId,
        @Schema(description = "판매자명", example = "판매자A") String sellerName,
        @Schema(description = "상품 ID", example = "67890") Long productId,
        @Schema(description = "상품 가격 정보") CartSearchPriceV1ApiResponse price,
        @Schema(description = "요청 수량", example = "2") Integer quantity,
        @Schema(description = "재고 수량", example = "2") Integer stockQuantity,
        @Schema(description = "옵션명", example = "화이트/M") String optionValue,
        @Schema(description = "장바구니 ID", example = "1") Long cartId,
        @Schema(description = "썸네일 이미지 URL", example = "https://cdn.example.com/image.jpg")
                String imageUrl,
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "상품 상태") CartSearchProductStatusV1ApiResponse productStatus,
        @Schema(description = "마일리지 비율", example = "19000") Double mileageRate,
        @Schema(description = "예상 적립 마일리지 금액", example = "29000") Double expectedMileageAmount,
        @Schema(description = "상품 카테고리 정보") List<CartSearchCategoryV1ApiResponse> categories) {

    @Schema(description = "장바구니 상품 상태")
    public record CartSearchPriceV1ApiResponse(
            @Schema(description = "정가", example = "29000") Long regularPrice,
            @Schema(description = "정가", example = "29000") Long currentPrice,
            @Schema(description = "정가", example = "29000") Long salePrice,
            @Schema(description = "할인가", example = "19000") Long directDiscountPrice,
            @Schema(description = "할인율 (%)", example = "34") Integer discountRate,
            @Schema(description = "할인율 (%)", example = "34") Integer directDiscountRate) {}

    @Schema(description = "장바구니 상품 상태")
    public record CartSearchProductStatusV1ApiResponse(
            @Schema(description = "품절 여부", example = "N") String soldOutYn,
            @Schema(description = "전시 여부", example = "Y") String displayYn) {}

    @Schema(description = "상품 카테고리 정보")
    public record CartSearchCategoryV1ApiResponse(
            @Schema(description = "카테고리 ID", example = "N") Long categoryId,
            @Schema(description = "카테고리 명", example = "Y") String categoryName,
            @Schema(description = "카테고리 전시 명", example = "Y") String displayName,
            @Schema(description = "부모 카테고리 ID", example = "Y") Long parentCategoryId,
            @Schema(description = "카테고리 댑스", example = "Y") Integer categoryDepth) {}
}
