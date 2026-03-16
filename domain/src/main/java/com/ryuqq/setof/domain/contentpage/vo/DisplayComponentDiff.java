package com.ryuqq.setof.domain.contentpage.vo;

import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DisplayComponent 변경 비교 결과.
 *
 * <p>기존 컴포넌트 컬렉션과 새 요청을 ID 기반으로 비교하여 추가/삭제/유지 목록을 제공합니다.
 *
 * <p>added: 신규 생성할 DisplayComponent (idValue == null). removed: soft delete 처리 완료 상태. retained: 속성
 * 갱신 완료 상태.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DisplayComponentDiff(
        List<DisplayComponent> added,
        List<DisplayComponent> removed,
        List<DisplayComponent> retained,
        Instant occurredAt) {

    public DisplayComponentDiff {
        added = List.copyOf(added);
        removed = List.copyOf(removed);
        retained = List.copyOf(retained);
    }

    /** 변경 없음을 나타내는 빈 Diff를 반환합니다. */
    public static DisplayComponentDiff empty() {
        return new DisplayComponentDiff(List.of(), List.of(), List.of(), Instant.EPOCH);
    }

    /**
     * 기존 컴포넌트와 새 컴포넌트를 비교하여 diff를 계산합니다.
     *
     * <p>새 컴포넌트의 idValue가 null이면 추가, 기존 ID와 매칭되면 유지(수정), 기존에만 있으면 삭제.
     *
     * @param existing 기존 컴포넌트 목록
     * @param incoming 새 컴포넌트 목록 (added에는 id가 없고, retained에는 기존 id가 있음)
     * @param now 변경 시각
     * @return DisplayComponentDiff
     */
    public static DisplayComponentDiff compute(
            List<DisplayComponent> existing, List<DisplayComponent> incoming, Instant now) {

        Map<Long, DisplayComponent> existingMap =
                existing.stream()
                        .collect(Collectors.toMap(DisplayComponent::idValue, Function.identity()));

        Set<Long> incomingIds =
                incoming.stream()
                        .filter(c -> c.idValue() != null)
                        .map(DisplayComponent::idValue)
                        .collect(Collectors.toSet());

        List<DisplayComponent> added = new ArrayList<>();
        List<DisplayComponent> retained = new ArrayList<>();

        for (DisplayComponent inc : incoming) {
            if (inc.idValue() == null) {
                added.add(inc);
            } else if (existingMap.containsKey(inc.idValue())) {
                retained.add(inc);
            } else {
                added.add(inc);
            }
        }

        List<DisplayComponent> removed = new ArrayList<>();
        for (DisplayComponent ex : existing) {
            if (!incomingIds.contains(ex.idValue())) {
                ex.remove(now);
                removed.add(ex);
            }
        }

        return new DisplayComponentDiff(added, removed, retained, now);
    }

    public boolean hasNoChanges() {
        return added.isEmpty() && removed.isEmpty() && retained.isEmpty();
    }

    /** retained + removed: dirty check 대상 일괄 persist용. */
    public List<DisplayComponent> allDirtyComponents() {
        List<DisplayComponent> result = new ArrayList<>(retained.size() + removed.size());
        result.addAll(retained);
        result.addAll(removed);
        return result;
    }
}
