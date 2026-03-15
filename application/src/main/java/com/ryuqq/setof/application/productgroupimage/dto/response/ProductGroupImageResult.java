package com.ryuqq.setof.application.productgroupimage.dto.response;

import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;

/** 상품 그룹 이미지 조회 결과 DTO. */
public record ProductGroupImageResult(Long id, String imageUrl, String imageType, int sortOrder) {

    public static ProductGroupImageResult from(ProductGroupImage image) {
        return new ProductGroupImageResult(
                image.idValue(),
                image.imageUrlValue(),
                image.imageType().name(),
                image.sortOrder());
    }

    /**
     * Variant URL이 적용된 이미지 결과를 생성합니다.
     *
     * @param image 원본 이미지
     * @param resolvedUrl 해석된 URL (variant URL 또는 원본 URL)
     * @return enriched ProductGroupImageResult
     */
    public static ProductGroupImageResult from(ProductGroupImage image, String resolvedUrl) {
        return new ProductGroupImageResult(
                image.idValue(), resolvedUrl, image.imageType().name(), image.sortOrder());
    }

    /** ImageWithVariantsResult에서 변환합니다 (원본 URL 사용). */
    public static ProductGroupImageResult from(ImageWithVariantsResult imageWithVariants) {
        return new ProductGroupImageResult(
                imageWithVariants.imageId(),
                imageWithVariants.imageUrl(),
                imageWithVariants.imageType(),
                imageWithVariants.sortOrder());
    }

    /** ImageWithVariantsResult에서 변환합니다 (해석된 URL 사용). */
    public static ProductGroupImageResult from(
            ImageWithVariantsResult imageWithVariants, String resolvedUrl) {
        return new ProductGroupImageResult(
                imageWithVariants.imageId(),
                resolvedUrl,
                imageWithVariants.imageType(),
                imageWithVariants.sortOrder());
    }
}
