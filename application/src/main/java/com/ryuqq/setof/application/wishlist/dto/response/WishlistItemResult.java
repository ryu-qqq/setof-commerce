package com.ryuqq.setof.application.wishlist.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 찜 항목 조회 결과 DTO.
 *
 * <p>레거시 LegacyUserFavoriteResult 기반 변환.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param userFavoriteId 찜 ID
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 셀러 ID
 * @param productGroupName 상품 그룹명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param productImageUrl 상품 이미지 URL
 * @param regularPrice 정상가
 * @param currentPrice 판매가
 * @param discountRate 할인율
 * @param soldOutYn 품절 여부
 * @param displayYn 전시 여부
 * @param insertDate 등록일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record WishlistItemResult(
        long userFavoriteId,
        long productGroupId,
        long sellerId,
        String productGroupName,
        long brandId,
        String brandName,
        String productImageUrl,
        BigDecimal regularPrice,
        BigDecimal currentPrice,
        int discountRate,
        String soldOutYn,
        String displayYn,
        LocalDateTime insertDate) {

    public static WishlistItemResult of(
            long userFavoriteId,
            long productGroupId,
            long sellerId,
            String productGroupName,
            long brandId,
            String brandName,
            String productImageUrl,
            BigDecimal regularPrice,
            BigDecimal currentPrice,
            int discountRate,
            String soldOutYn,
            String displayYn,
            LocalDateTime insertDate) {
        return new WishlistItemResult(
                userFavoriteId,
                productGroupId,
                sellerId,
                productGroupName,
                brandId,
                brandName,
                productImageUrl,
                regularPrice,
                currentPrice,
                discountRate,
                soldOutYn,
                displayYn,
                insertDate);
    }

    public boolean isSoldOut() {
        return "Y".equals(soldOutYn);
    }

    public boolean isDisplayed() {
        return "Y".equals(displayYn);
    }
}
