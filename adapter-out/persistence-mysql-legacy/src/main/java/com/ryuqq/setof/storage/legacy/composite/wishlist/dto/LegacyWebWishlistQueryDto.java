package com.ryuqq.setof.storage.legacy.composite.wishlist.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebWishlistQueryDto - 레거시 찜 목록 복합 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>user_favorite + product_group + product_group_image + brand JOIN 결과.
 *
 * @param userFavoriteId 찜 ID
 * @param productGroupId 상품그룹 ID
 * @param sellerId 판매자 ID
 * @param productGroupName 상품그룹명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param imageUrl 대표 이미지 URL
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param soldOutYn 품절 여부
 * @param displayYn 전시 여부
 * @param insertDate 상품 등록일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebWishlistQueryDto(
        long userFavoriteId,
        long productGroupId,
        long sellerId,
        String productGroupName,
        long brandId,
        String brandName,
        String imageUrl,
        int regularPrice,
        int currentPrice,
        String soldOutYn,
        String displayYn,
        LocalDateTime insertDate) {

    public int discountRate() {
        if (regularPrice <= 0 || currentPrice >= regularPrice) {
            return 0;
        }
        return (int) ((long) (regularPrice - currentPrice) * 100 / regularPrice);
    }
}
