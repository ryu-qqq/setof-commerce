package com.connectly.partnerAdmin.module.discount.service;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.dto.query.CreateDiscountTarget;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import com.connectly.partnerAdmin.module.discount.entity.DiscountTarget;
import com.connectly.partnerAdmin.module.discount.repository.DiscountTargetRepository;
import com.connectly.partnerAdmin.module.discount.service.fetch.DiscountFetchService;
import com.connectly.partnerAdmin.module.discount.service.fetch.DiscountTargetFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class DiscountTargetQueryServiceImpl implements DiscountTargetQueryService{

    private final DiscountTargetFetchService discountTargetFindService;
    private final DiscountTargetRepository discountTargetRepository;
    private final DiscountFetchService discountFetchService;
    private final DiscountRedisQueryService discountRedisQueryService;

    @Override
    public List<DiscountTarget> createDiscountTargets(long discountPolicyId, CreateDiscountTarget createDiscountTarget) {
        DiscountPolicy discountPolicy = discountFetchService.fetchDiscountEntity(discountPolicyId);
        return saveDiscountTargetsToCacheAndDb(discountPolicy, createDiscountTarget);
    }

    @Override
    public List<DiscountTarget> updateDiscountTargets(long discountPolicyId, CreateDiscountTarget createDiscountTarget) {
        List<DiscountTarget> discountTargets = discountTargetFindService.fetchDiscountTargetEntities(Collections.singletonList(discountPolicyId));
        return processUpdateDiscountTarget(discountPolicyId, discountTargets, createDiscountTarget.getTargetIds());
    }

    @Override
    public void updateDiscountTargetUseYn(List<DiscountPolicy> discountPolicies, Yn activeYn) {
        List<Long> discountPolicyIds = discountPolicies.stream()
                .map(DiscountPolicy::getId)
                .collect(Collectors.toList());

        List<DiscountTarget> discountTargets = discountTargetFindService.fetchDiscountTargetEntities(discountPolicyIds);

        Map<Long, List<DiscountTarget>> discountPolicyIdMap = discountTargets.stream().collect(Collectors.groupingBy(DiscountTarget::getDiscountPolicyId));

        for(DiscountPolicy discountPolicy : discountPolicies){
            List<DiscountTarget> targets = discountPolicyIdMap.get(discountPolicy.getId());
            if(targets != null && !targets.isEmpty()) {
                targets.forEach(discountTarget -> discountTarget.setActiveYn(activeYn));
                updateDiscountCache(discountPolicy, targets);
            }
        }
    }

    private List<DiscountTarget> saveDiscountTargetsToCacheAndDb(DiscountPolicy discountPolicy, CreateDiscountTarget createDiscountTarget){
        Set<DiscountTarget> discountTargets = createDiscountTarget.getTargetIds().stream()
                .map(targetId -> new DiscountTarget(discountPolicy, targetId))
                .collect(Collectors.toSet());

        List<DiscountTarget> savedDiscountTargets = discountTargetRepository.saveAll(discountTargets);
        updateDiscountCache(discountPolicy, savedDiscountTargets);
        return savedDiscountTargets;
    }



    private List<DiscountTarget> processUpdateDiscountTarget(long discountPolicyId, List<DiscountTarget> discountTargets, List<Long> targetIds) {
        Map<Long, DiscountTarget> discountTargetMap = toDiscountTargetMap(discountTargets);
        List<DiscountTarget> newDiscountTargets = new ArrayList<>();
        List<DiscountTarget> updateDiscountTargets = new ArrayList<>();


        DiscountPolicy discountPolicy = discountFetchService.fetchDiscountEntity(discountPolicyId);

        for (Long targetId : targetIds) {
            DiscountTarget discountTarget = discountTargetMap.get(targetId);
            if (discountTarget == null) {
                newDiscountTargets.add(new DiscountTarget(discountPolicy, targetId));
            } else {
                if (discountTarget.getActiveYn().isNo()) {
                    discountTarget.setActiveYn(Yn.Y);
                    updateDiscountTargets.add(discountTarget);
                }
                discountTargetMap.remove(targetId);
            }
        }

        for (DiscountTarget remainingTarget : discountTargetMap.values()) {
            remainingTarget.setActiveYn(Yn.N);
            updateDiscountTargets.add(remainingTarget);
        }

        List<DiscountTarget> savedDiscountTargets = discountTargetRepository.saveAll(newDiscountTargets);
        savedDiscountTargets.addAll(updateDiscountTargets);

        updateDiscountCache(discountPolicy, savedDiscountTargets);

        return savedDiscountTargets;
    }



    private Map<Long, DiscountTarget> toDiscountTargetMap(List<DiscountTarget> discountTargets) {
        return discountTargets.stream()
                .collect(Collectors.toMap(DiscountTarget::getTargetId,
                        Function.identity(), (existing, replacement) -> existing));

    }


    private void updateDiscountCache(DiscountPolicy discountPolicy, List<DiscountTarget> savedDiscountTargets){
        discountRedisQueryService.updateDiscountCache(discountPolicy, savedDiscountTargets);
    }

}
