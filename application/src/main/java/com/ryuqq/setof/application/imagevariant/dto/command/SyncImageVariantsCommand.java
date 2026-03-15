package com.ryuqq.setof.application.imagevariant.dto.command;

import java.util.List;

/**
 * 이미지 Variant 동기화 커맨드.
 *
 * @param sourceImageId 원본 이미지 ID
 * @param sourceType 이미지 소스 타입 (PRODUCT_GROUP_IMAGE, DESCRIPTION_IMAGE)
 * @param variants Variant 목록
 */
public record SyncImageVariantsCommand(
        long sourceImageId, String sourceType, List<VariantCommand> variants) {

    /**
     * Variant 데이터.
     *
     * @param variantType Variant 타입 (SMALL_WEBP, MEDIUM_WEBP, LARGE_WEBP, ORIGINAL_WEBP)
     * @param resultAssetId FileFlow 변환 결과 에셋 ID
     * @param variantUrl 변환된 이미지 CDN URL
     * @param width 이미지 너비 (px)
     * @param height 이미지 높이 (px)
     */
    public record VariantCommand(
            String variantType,
            String resultAssetId,
            String variantUrl,
            Integer width,
            Integer height) {}
}
