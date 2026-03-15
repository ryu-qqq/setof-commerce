package com.ryuqq.setof.application.productgroupimage.dto.response;

import java.util.List;

/**
 * ImageWithVariantsResult - 이미지 + Variant URL 통합 조회 결과.
 *
 * <p>product_group_images LEFT JOIN image_variants 결과를 그룹화한 DTO입니다.
 *
 * @param imageId 이미지 ID
 * @param imageType 이미지 유형 (THUMBNAIL, DETAIL)
 * @param imageUrl 원본 이미지 URL
 * @param sortOrder 정렬 순서
 * @param variants 해당 이미지의 Variant 목록 (없으면 빈 리스트)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ImageWithVariantsResult(
        Long imageId,
        String imageType,
        String imageUrl,
        int sortOrder,
        List<VariantInfo> variants) {

    /**
     * Variant 정보.
     *
     * @param variantType Variant 타입 (SMALL_WEBP, MEDIUM_WEBP, LARGE_WEBP, ORIGINAL_WEBP)
     * @param variantUrl 변환된 이미지 CDN URL
     * @param width 너비 (px)
     * @param height 높이 (px)
     */
    public record VariantInfo(
            String variantType, String variantUrl, Integer width, Integer height) {}

    /** 특정 타입의 variant URL을 찾습니다. 없으면 원본 URL을 반환합니다. */
    public String resolveUrl(String preferredVariantType) {
        return variants.stream()
                .filter(v -> v.variantType().equals(preferredVariantType))
                .findFirst()
                .map(VariantInfo::variantUrl)
                .orElse(imageUrl);
    }

    /** variant가 있는지 여부. */
    public boolean hasVariants() {
        return variants != null && !variants.isEmpty();
    }
}
