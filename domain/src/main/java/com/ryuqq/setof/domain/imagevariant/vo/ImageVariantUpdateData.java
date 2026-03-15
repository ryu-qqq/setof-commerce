package com.ryuqq.setof.domain.imagevariant.vo;

import java.time.Instant;

/**
 * ImageVariant 수정 데이터.
 *
 * <p>수정할 새 variant 목록과 수정 시각을 불변으로 보관합니다.
 */
public class ImageVariantUpdateData {

    private final ImageVariants newVariants;
    private final Instant updatedAt;

    private ImageVariantUpdateData(ImageVariants newVariants, Instant updatedAt) {
        this.newVariants = newVariants;
        this.updatedAt = updatedAt;
    }

    public static ImageVariantUpdateData of(ImageVariants newVariants, Instant updatedAt) {
        return new ImageVariantUpdateData(newVariants, updatedAt);
    }

    public ImageVariants newVariants() {
        return newVariants;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
