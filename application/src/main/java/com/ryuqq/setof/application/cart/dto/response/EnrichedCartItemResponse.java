package com.ryuqq.setof.application.cart.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Enriched 장바구니 아이템 응답 DTO
 *
 * <p>상품 상세 정보가 포함된 장바구니 아이템 응답입니다. V1 Legacy API 호환성을 위해 생성되었습니다.
 *
 * @param cartItemId 장바구니 아이템 ID
 * @param productStockId 상품 재고 ID (SKU)
 * @param productId 상품 ID
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 판매자 ID
 * @param quantity 수량
 * @param unitPrice 단가
 * @param totalPrice 총 가격 (수량 * 단가)
 * @param selected 선택 여부
 * @param addedAt 추가 시각
 * @param productGroupName 상품그룹명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param sellerName 셀러명
 * @param optionValue 옵션값 (예: "270mm / Black")
 * @param imageUrl 대표 이미지 URL
 * @param stockQuantity 재고 수량
 * @param soldOut 품절 여부
 * @param regularPrice 정가
 * @param salePrice 판매가
 * @param discountRate 할인율
 * @param categories 카테고리 정보
 * @author development-team
 * @since 1.0.0
 */
public record EnrichedCartItemResponse(
        Long cartItemId,
        Long productStockId,
        Long productId,
        Long productGroupId,
        Long sellerId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        boolean selected,
        Instant addedAt,
        String productGroupName,
        Long brandId,
        String brandName,
        String sellerName,
        String optionValue,
        String imageUrl,
        Integer stockQuantity,
        boolean soldOut,
        BigDecimal regularPrice,
        BigDecimal salePrice,
        int discountRate,
        List<CategoryInfo> categories) {

    /**
     * 카테고리 정보
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     * @param depth 카테고리 뎁스
     */
    public record CategoryInfo(Long categoryId, String categoryName, int depth) {}

    /**
     * 기본 CartItemResponse로부터 Enriched 응답 생성
     *
     * @param item 기본 장바구니 아이템 응답
     * @param productGroupName 상품그룹명
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명
     * @param sellerName 셀러명
     * @param optionValue 옵션값
     * @param imageUrl 대표 이미지 URL
     * @param stockQuantity 재고 수량
     * @param soldOut 품절 여부
     * @param regularPrice 정가
     * @param salePrice 판매가
     * @param discountRate 할인율
     * @param categories 카테고리 정보
     * @return EnrichedCartItemResponse
     */
    public static EnrichedCartItemResponse from(
            CartItemResponse item,
            String productGroupName,
            Long brandId,
            String brandName,
            String sellerName,
            String optionValue,
            String imageUrl,
            Integer stockQuantity,
            boolean soldOut,
            BigDecimal regularPrice,
            BigDecimal salePrice,
            int discountRate,
            List<CategoryInfo> categories) {
        return new EnrichedCartItemResponse(
                item.cartItemId(),
                item.productStockId(),
                item.productId(),
                item.productGroupId(),
                item.sellerId(),
                item.quantity(),
                item.unitPrice(),
                item.totalPrice(),
                item.selected(),
                item.addedAt(),
                productGroupName,
                brandId,
                brandName,
                sellerName,
                optionValue,
                imageUrl,
                stockQuantity,
                soldOut,
                regularPrice,
                salePrice,
                discountRate,
                categories);
    }
}
