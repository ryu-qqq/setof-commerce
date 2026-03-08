package com.ryuqq.setof.storage.legacy.composite.web.cart.dto;

import java.util.Set;

/**
 * LegacyWebCartQueryDto - 레거시 장바구니 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>GroupBy로 옵션을 집합으로 조회하여 N+1 문제 방지.
 *
 * @param cartId 장바구니 ID
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param sellerId 판매자 ID
 * @param sellerName 판매자명
 * @param productId 상품(SKU) ID
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param salePrice 할인가
 * @param directDiscountRate 직접 할인율
 * @param directDiscountPrice 직접 할인가
 * @param discountRate 전체 할인율
 * @param quantity 장바구니 수량
 * @param stockQuantity 재고 수량
 * @param imageUrl 대표 이미지 URL
 * @param soldOutYn 품절 여부
 * @param displayYn 표시 여부
 * @param categoryPath 카테고리 경로
 * @param options 옵션 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebCartQueryDto(
        long cartId,
        long brandId,
        String brandName,
        long productGroupId,
        String productGroupName,
        long sellerId,
        String sellerName,
        long productId,
        int regularPrice,
        int currentPrice,
        int salePrice,
        int directDiscountRate,
        int directDiscountPrice,
        int discountRate,
        int quantity,
        int stockQuantity,
        String imageUrl,
        String soldOutYn,
        String displayYn,
        String categoryPath,
        Set<LegacyWebCartOptionQueryDto> options) {

    /**
     * 상품 상태 문자열 반환.
     *
     * @return 상품 상태 (ON_SALE, SOLD_OUT, HIDDEN)
     */
    public String getProductStatus() {
        if ("Y".equals(soldOutYn)) {
            return "SOLD_OUT";
        }
        if ("N".equals(displayYn)) {
            return "HIDDEN";
        }
        return "ON_SALE";
    }
}
