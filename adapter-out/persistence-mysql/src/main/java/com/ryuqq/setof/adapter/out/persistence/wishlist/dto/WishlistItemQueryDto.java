package com.ryuqq.setof.adapter.out.persistence.wishlist.dto;

import java.time.Instant;

/**
 * WishlistItemQueryDto - 찜 항목 복합 조회 내부 DTO.
 *
 * <p>QueryDSL Projections.constructor()를 통해 생성됩니다.
 *
 * <p>ProductGroup 상태(status)와 가격(int)은 Mapper에서 WishlistItemResult로 변환됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class WishlistItemQueryDto {

    private final long wishlistItemId;
    private final long productGroupId;
    private final long sellerId;
    private final String productGroupName;
    private final long brandId;
    private final String brandName;
    private final String productImageUrl;
    private final int regularPrice;
    private final int currentPrice;
    private final int discountRate;
    private final String status;
    private final boolean brandDisplayed;
    private final Instant createdAt;

    public WishlistItemQueryDto(
            long wishlistItemId,
            long productGroupId,
            long sellerId,
            String productGroupName,
            long brandId,
            String brandName,
            String productImageUrl,
            int regularPrice,
            int currentPrice,
            int discountRate,
            String status,
            boolean brandDisplayed,
            Instant createdAt) {
        this.wishlistItemId = wishlistItemId;
        this.productGroupId = productGroupId;
        this.sellerId = sellerId;
        this.productGroupName = productGroupName;
        this.brandId = brandId;
        this.brandName = brandName;
        this.productImageUrl = productImageUrl;
        this.regularPrice = regularPrice;
        this.currentPrice = currentPrice;
        this.discountRate = discountRate;
        this.status = status;
        this.brandDisplayed = brandDisplayed;
        this.createdAt = createdAt;
    }

    public long getWishlistItemId() {
        return wishlistItemId;
    }

    public long getProductGroupId() {
        return productGroupId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public long getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getRegularPrice() {
        return regularPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public String getStatus() {
        return status;
    }

    public boolean isBrandDisplayed() {
        return brandDisplayed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
