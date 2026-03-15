package com.ryuqq.setof.domain.banner.vo;

import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * BannerSlide 변경 비교 결과.
 *
 * <p>기존 슬라이드 컬렉션과 새 요청을 ID 기반으로 비교하여 추가/삭제/유지 목록을 제공합니다.
 *
 * <p>added: 신규 생성할 BannerSlide (slideId == null). removed: soft delete 처리 완료 상태. retained: 속성 갱신 완료
 * 상태.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public record BannerSlideDiff(
        List<BannerSlide> added,
        List<BannerSlide> removed,
        List<BannerSlide> retained,
        Instant occurredAt) {

    public BannerSlideDiff {
        added = List.copyOf(added);
        removed = List.copyOf(removed);
        retained = List.copyOf(retained);
    }

    public static BannerSlideDiff of(
            List<BannerSlide> added,
            List<BannerSlide> removed,
            List<BannerSlide> retained,
            Instant occurredAt) {
        return new BannerSlideDiff(added, removed, retained, occurredAt);
    }

    public boolean hasNoChanges() {
        return added.isEmpty() && removed.isEmpty();
    }

    /** retained + removed: dirty check 대상 일괄 persist용. */
    public List<BannerSlide> allDirtySlides() {
        List<BannerSlide> result = new ArrayList<>(retained.size() + removed.size());
        result.addAll(retained);
        result.addAll(removed);
        return result;
    }
}
