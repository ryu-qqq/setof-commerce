package com.ryuqq.setof.domain.contentpage.vo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ProductThumbnailSnapshot - 디스플레이 컴포넌트 내 상품그룹 썸네일 스냅샷.
 *
 * <p>PRODUCT/BRAND/CATEGORY/TAB 컴포넌트에 포함되는 상품 썸네일 정보. displayName/displayImageUrl이 non-null이면 전시 영역
 * 전용 오버라이드, null이면 원본 상품 정보(productGroupName/productImageUrl)를 사용한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductThumbnailSnapshot(
        long productGroupId,
        long sellerId,
        String productGroupName,
        long brandId,
        String brandName,
        String productImageUrl,
        String displayName,
        String displayImageUrl,
        int regularPrice,
        int currentPrice,
        int salePrice,
        int directDiscountRate,
        int directDiscountPrice,
        int discountRate,
        Instant createdAt,
        boolean displayed,
        boolean soldOut) {

    /** 전시 영역에 노출할 상품명을 반환한다. displayName이 있으면 오버라이드, 없으면 원본 productGroupName. */
    public String resolvedName() {
        return (displayName != null && !displayName.isBlank()) ? displayName : productGroupName;
    }

    /** 전시 영역에 노출할 상품 이미지를 반환한다. displayImageUrl이 있으면 오버라이드, 없으면 원본 productImageUrl. */
    public String resolvedImageUrl() {
        return (displayImageUrl != null && !displayImageUrl.isBlank())
                ? displayImageUrl
                : productImageUrl;
    }

    /**
     * FIXED + AUTO 상품 병합 및 정렬.
     *
     * <p>레거시 정렬 규칙: FIXED(원래 순서 유지) → AUTO(OrderType 기반 정렬, FIXED 중복 제거) → pageSize 제한.
     *
     * @param fixed FIXED 상품 목록 (displayOrder ASC 정렬 상태)
     * @param auto AUTO 상품 목록
     * @param orderType AUTO 상품에 적용할 정렬 방식
     * @param pageSize 최대 반환 수
     * @return 병합된 상품 목록
     */
    public static List<ProductThumbnailSnapshot> mergeFixedAndAuto(
            List<ProductThumbnailSnapshot> fixed,
            List<ProductThumbnailSnapshot> auto,
            OrderType orderType,
            int pageSize) {
        Set<Long> fixedIds =
                fixed.stream()
                        .map(ProductThumbnailSnapshot::productGroupId)
                        .collect(Collectors.toSet());

        List<ProductThumbnailSnapshot> filteredAuto =
                auto.stream()
                        .filter(p -> !fixedIds.contains(p.productGroupId()))
                        .sorted(orderType.comparator())
                        .toList();

        List<ProductThumbnailSnapshot> merged = new ArrayList<>(fixed.size() + filteredAuto.size());
        merged.addAll(fixed);
        merged.addAll(filteredAuto);

        return merged.subList(0, Math.min(merged.size(), pageSize));
    }
}
