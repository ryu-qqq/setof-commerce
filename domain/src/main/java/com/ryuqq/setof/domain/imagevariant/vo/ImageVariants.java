package com.ryuqq.setof.domain.imagevariant.vo;

import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 이미지 Variant 컬렉션 VO.
 *
 * <p>동일 sourceImageId에 속하는 ImageVariant 목록을 관리합니다. variantType + variantUrl 조합을 키로 사용하여 diff를
 * 수행합니다.
 */
public class ImageVariants {

    private final List<ImageVariant> variants;

    private ImageVariants(List<ImageVariant> variants) {
        this.variants = variants;
    }

    /** 신규 생성 시 사용. */
    public static ImageVariants of(List<ImageVariant> variants) {
        if (variants == null || variants.isEmpty()) {
            return new ImageVariants(List.of());
        }
        return new ImageVariants(List.copyOf(variants));
    }

    /** 영속성에서 복원 시 사용. */
    public static ImageVariants reconstitute(List<ImageVariant> variants) {
        if (variants == null || variants.isEmpty()) {
            return new ImageVariants(List.of());
        }
        return new ImageVariants(List.copyOf(variants));
    }

    // === 조회 ===

    public List<ImageVariant> toList() {
        return Collections.unmodifiableList(variants);
    }

    public boolean isEmpty() {
        return variants.isEmpty();
    }

    public int size() {
        return variants.size();
    }

    // === Update ===

    /**
     * 새 variant 목록과 비교하여 추가/삭제/유지를 판단하고 상태를 갱신합니다.
     *
     * <p>variantType + variantUrl 조합을 키로 비교합니다. 유지 대상은 변경 없이 유지하고, 삭제 대상은 soft delete 처리합니다.
     *
     * @param updateData 수정할 새 variant 목록과 수정 시각
     * @return 변경 비교 결과
     */
    public ImageVariantDiff update(ImageVariantUpdateData updateData) {
        Map<String, ImageVariant> existingByKey =
                variants.stream().collect(Collectors.toMap(ImageVariants::variantKey, v -> v));

        List<ImageVariant> added = new ArrayList<>();
        List<ImageVariant> retained = new ArrayList<>();
        Set<String> newKeys = new HashSet<>();

        for (ImageVariant newVariant : updateData.newVariants().toList()) {
            String key = variantKey(newVariant);
            newKeys.add(key);

            ImageVariant existing = existingByKey.get(key);
            if (existing != null) {
                retained.add(existing);
            } else {
                added.add(newVariant);
            }
        }

        List<ImageVariant> removed =
                variants.stream().filter(v -> !newKeys.contains(variantKey(v))).toList();

        for (ImageVariant variant : removed) {
            variant.delete(updateData.updatedAt());
        }

        return ImageVariantDiff.of(added, removed, retained, updateData.updatedAt());
    }

    private static String variantKey(ImageVariant variant) {
        return variant.variantType().name() + "::" + variant.variantUrlValue();
    }
}
