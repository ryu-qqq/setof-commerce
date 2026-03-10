package com.ryuqq.setof.application.selleroption.internal;

import com.ryuqq.setof.application.selleroption.manager.SellerOptionGroupCommandManager;
import com.ryuqq.setof.application.selleroption.manager.SellerOptionValueCommandManager;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroupDiff;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SellerOptionPersistFacade {

    private final SellerOptionGroupCommandManager groupCommandManager;
    private final SellerOptionValueCommandManager valueCommandManager;

    public SellerOptionPersistFacade(
            SellerOptionGroupCommandManager groupCommandManager,
            SellerOptionValueCommandManager valueCommandManager) {
        this.groupCommandManager = groupCommandManager;
        this.valueCommandManager = valueCommandManager;
    }

    @Transactional
    public List<SellerOptionValueId> persistAll(List<SellerOptionGroup> optionGroups) {
        return optionGroups.stream()
                .flatMap(
                        group -> {
                            Long groupId = groupCommandManager.persist(group);
                            return valueCommandManager
                                    .persistAllForGroup(groupId, group.optionValues())
                                    .stream();
                        })
                .map(SellerOptionValueId::of)
                .toList();
    }

    @Transactional
    public List<SellerOptionValueId> persistDiff(SellerOptionGroupDiff diff) {
        // 1. 기존 그룹 (removed + retained) 일괄 persist
        List<SellerOptionGroup> existingGroups = new ArrayList<>();
        existingGroups.addAll(diff.removedGroups());
        diff.retainedGroups().forEach(r -> existingGroups.add(r.group()));
        if (!existingGroups.isEmpty()) {
            groupCommandManager.persistAll(existingGroups);
        }

        // 2. 삭제 대상 값 일괄 persist (soft delete dirty check)
        valueCommandManager.persistAll(diff.allRemovedValues());

        // 3. 신규 그룹: 개별 persist → 생성 ID 획득 → 해당 값을 persistAllForGroup
        List<SellerOptionValue> allAddedValueInstances = new ArrayList<>();
        List<Long> allAddedValueIds = new ArrayList<>();

        for (SellerOptionGroup addedGroup : diff.addedGroups()) {
            Long groupId = groupCommandManager.persist(addedGroup);
            List<Long> valueIds =
                    valueCommandManager.persistAllForGroup(groupId, addedGroup.optionValues());
            allAddedValueInstances.addAll(addedGroup.optionValues());
            allAddedValueIds.addAll(valueIds);
        }

        // 4. 유지 그룹의 신규 값
        for (SellerOptionGroupDiff.RetainedGroupDiff retained : diff.retainedGroups()) {
            List<SellerOptionValue> addedInRetained = retained.addedValues();
            if (!addedInRetained.isEmpty()) {
                Long groupId = retained.group().idValue();
                List<Long> valueIds =
                        valueCommandManager.persistAllForGroup(groupId, addedInRetained);
                allAddedValueInstances.addAll(addedInRetained);
                allAddedValueIds.addAll(valueIds);
            }
        }

        Map<SellerOptionValueId, Long> generatedIdMap =
                buildGeneratedIdMap(allAddedValueInstances, allAddedValueIds);

        // 5. 유지 값 일괄 persist (속성 변경 dirty check)
        valueCommandManager.persistAll(diff.allRetainedValues());

        // 6. orderedActiveValueIds의 null ID를 실제 생성 ID로 치환
        return resolveActiveValueIds(diff.orderedActiveValueIds(), generatedIdMap);
    }

    private Map<SellerOptionValueId, Long> buildGeneratedIdMap(
            List<SellerOptionValue> values, List<Long> generatedIds) {
        Map<SellerOptionValueId, Long> map = new IdentityHashMap<>();
        for (int i = 0; i < values.size(); i++) {
            map.put(values.get(i).id(), generatedIds.get(i));
        }
        return map;
    }

    private List<SellerOptionValueId> resolveActiveValueIds(
            List<SellerOptionValueId> orderedIds, Map<SellerOptionValueId, Long> generatedIdMap) {
        return orderedIds.stream()
                .map(
                        id -> {
                            if (!id.isNew()) {
                                return id;
                            }
                            Long generatedId = generatedIdMap.get(id);
                            if (generatedId == null) {
                                throw new IllegalStateException(
                                        "신규 SellerOptionValueId에 대한 생성된 ID를 찾을 수 없습니다");
                            }
                            return SellerOptionValueId.of(generatedId);
                        })
                .toList();
    }
}
