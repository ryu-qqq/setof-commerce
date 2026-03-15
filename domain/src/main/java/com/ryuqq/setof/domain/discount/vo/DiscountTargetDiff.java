package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * DiscountTarget 변경 비교 결과.
 *
 * <p>기존 타겟 컬렉션과 새 요청을 targetType + targetId 기반으로 비교하여 추가/삭제/유지 목록을 제공합니다.
 *
 * <p>added: 신규 생성할 DiscountTarget (요청에 있으나 기존에 없음). removed: soft delete 처리 완료 상태 (기존에 있으나 요청에 없음).
 * retained: 변경 없이 유지되는 상태 (기존에 있고 요청에도 있음).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DiscountTargetDiff(
        List<DiscountTarget> added,
        List<DiscountTarget> removed,
        List<DiscountTarget> retained,
        Instant occurredAt) {

    public DiscountTargetDiff {
        added = List.copyOf(added);
        removed = List.copyOf(removed);
        retained = List.copyOf(retained);
    }

    public static DiscountTargetDiff of(
            List<DiscountTarget> added,
            List<DiscountTarget> removed,
            List<DiscountTarget> retained,
            Instant occurredAt) {
        return new DiscountTargetDiff(added, removed, retained, occurredAt);
    }

    /** 변경이 없는지 확인. retained만 있으면 변경 없음. */
    public boolean hasNoChanges() {
        return added.isEmpty() && removed.isEmpty();
    }

    /** retained + removed: UPDATE 대상 일괄 persist용. */
    public List<DiscountTarget> allDirtyTargets() {
        List<DiscountTarget> result = new ArrayList<>(retained.size() + removed.size());
        result.addAll(retained);
        result.addAll(removed);
        return result;
    }

    /** added + removed: 아웃박스 생성 대상 (실제 변경된 타겟). */
    public List<DiscountTarget> allChangedTargets() {
        List<DiscountTarget> result = new ArrayList<>(added.size() + removed.size());
        result.addAll(added);
        result.addAll(removed);
        return result;
    }
}
