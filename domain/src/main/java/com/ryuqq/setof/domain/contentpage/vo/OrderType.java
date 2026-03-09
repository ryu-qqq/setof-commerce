package com.ryuqq.setof.domain.contentpage.vo;

import java.util.Comparator;

/**
 * 상품 정렬 방식.
 *
 * <p>각 정렬 타입은 {@link ProductThumbnailSnapshot}에 대한 {@link Comparator}를 제공한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum OrderType {
    NONE,
    RECOMMEND,
    REVIEW,
    RECENT,
    HIGH_RATING,
    LOW_PRICE,
    HIGH_PRICE,
    LOW_DISCOUNT,
    HIGH_DISCOUNT;

    /** AUTO 상품에 적용할 정렬 Comparator를 반환한다. */
    public Comparator<ProductThumbnailSnapshot> comparator() {
        return switch (this) {
            case RECOMMEND ->
                    Comparator.comparingDouble(ProductThumbnailSnapshot::score).reversed();
            case REVIEW ->
                    Comparator.comparingLong(ProductThumbnailSnapshot::reviewCount).reversed();
            case RECENT ->
                    Comparator.comparingLong(ProductThumbnailSnapshot::productGroupId).reversed();
            case HIGH_RATING ->
                    Comparator.comparingDouble(ProductThumbnailSnapshot::averageRating).reversed();
            case LOW_PRICE -> Comparator.comparingInt(ProductThumbnailSnapshot::currentPrice);
            case HIGH_PRICE ->
                    Comparator.comparingInt(ProductThumbnailSnapshot::currentPrice).reversed();
            case LOW_DISCOUNT -> Comparator.comparingInt(ProductThumbnailSnapshot::discountRate);
            case HIGH_DISCOUNT ->
                    Comparator.comparingInt(ProductThumbnailSnapshot::discountRate).reversed();
            case NONE -> Comparator.comparingLong(ProductThumbnailSnapshot::productGroupId);
        };
    }
}
