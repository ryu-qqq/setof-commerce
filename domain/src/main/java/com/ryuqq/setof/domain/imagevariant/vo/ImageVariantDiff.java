package com.ryuqq.setof.domain.imagevariant.vo;

import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.time.Instant;
import java.util.List;

/**
 * ImageVariant 변경 비교 결과.
 *
 * <p>기존 variant와 새 variant를 variantType + variantUrl 기준으로 비교하여 추가/삭제/유지 목록을 제공합니다.
 */
public record ImageVariantDiff(
        List<ImageVariant> added,
        List<ImageVariant> removed,
        List<ImageVariant> retained,
        Instant occurredAt) {

    public ImageVariantDiff {
        added = List.copyOf(added);
        removed = List.copyOf(removed);
        retained = List.copyOf(retained);
    }

    public static ImageVariantDiff of(
            List<ImageVariant> added,
            List<ImageVariant> removed,
            List<ImageVariant> retained,
            Instant occurredAt) {
        return new ImageVariantDiff(added, removed, retained, occurredAt);
    }

    /** 변경 사항이 없는지 확인. */
    public boolean hasNoChanges() {
        return added.isEmpty() && removed.isEmpty();
    }
}
