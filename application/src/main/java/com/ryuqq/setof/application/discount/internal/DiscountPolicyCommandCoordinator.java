package com.ryuqq.setof.application.discount.internal;

import com.ryuqq.setof.application.discount.manager.DiscountOutboxCommandManager;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyCommandManager;
import com.ryuqq.setof.application.discount.manager.DiscountTargetCommandManager;
import com.ryuqq.setof.application.discount.validator.DiscountOutboxValidator;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetDiff;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DiscountPolicyCommandCoordinator - 할인 정책 명령 조율 Coordinator.
 *
 * <p>정책 생성/수정/타겟 변경 시 PolicyCommandManager, TargetCommandManager, OutboxCommandManager,
 * OutboxValidator 간의 흐름을 조율합니다.
 *
 * <p>APP-MGR-001: @Transactional은 Coordinator/Manager에서 선언하며, Service에서는 선언하지 않습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPolicyCommandCoordinator {

    private final DiscountPolicyCommandManager policyCommandManager;
    private final DiscountTargetCommandManager targetCommandManager;
    private final DiscountOutboxCommandManager outboxCommandManager;
    private final DiscountOutboxValidator outboxValidator;

    public DiscountPolicyCommandCoordinator(
            DiscountPolicyCommandManager policyCommandManager,
            DiscountTargetCommandManager targetCommandManager,
            DiscountOutboxCommandManager outboxCommandManager,
            DiscountOutboxValidator outboxValidator) {
        this.policyCommandManager = policyCommandManager;
        this.targetCommandManager = targetCommandManager;
        this.outboxCommandManager = outboxCommandManager;
        this.outboxValidator = outboxValidator;
    }

    /**
     * 정책 생성 + 타겟 저장 + 아웃박스 생성.
     *
     * <p>활성 타겟이 존재할 경우 일괄 저장하고, 각 타겟에 대해 중복 검증 후 아웃박스를 생성합니다. 시간 정보는 Factory에서 생성된
     * policy.createdAt()을 사용합니다.
     *
     * @param policy 생성할 할인 정책 (타겟 포함)
     * @return 생성된 정책 ID
     */
    @Transactional
    public long createPolicyWithTargets(DiscountPolicy policy) {
        long policyId = policyCommandManager.persist(policy);
        List<DiscountTarget> targets = policy.activeTargets();
        if (!targets.isEmpty()) {
            targetCommandManager.persistAll(policyId, targets);
            Instant createdAt = policy.createdAt();
            for (DiscountTarget target : targets) {
                createOutboxIfEligible(target, createdAt);
            }
        }
        return policyId;
    }

    /**
     * 정책 업데이트 + 아웃박스 생성 (기존 타겟에 대해).
     *
     * <p>정책 내용을 갱신하고, 현재 활성 타겟 전체에 대해 중복 검증 후 아웃박스를 생성합니다.
     *
     * @param policy 업데이트할 할인 정책
     */
    @Transactional
    public void updatePolicy(DiscountPolicy policy) {
        policyCommandManager.persist(policy);
        Instant changedAt = policy.updatedAt();
        for (DiscountTarget target : policy.activeTargets()) {
            createOutboxIfEligible(target, changedAt);
        }
    }

    /**
     * 타겟 변경 Diff 기반 persist + 아웃박스 생성.
     *
     * <p>DiscountTargetDiff의 added → INSERT, allDirtyTargets(retained + removed) → UPDATE. 변경된
     * 타겟(added + removed)에 대해 아웃박스를 생성합니다.
     *
     * @param policyId 대상 정책 ID
     * @param diff 타겟 변경 비교 결과
     */
    @Transactional
    public void persistTargetDiff(long policyId, DiscountTargetDiff diff) {
        if (diff.hasNoChanges()) {
            return;
        }

        targetCommandManager.persistDiff(policyId, diff);

        Instant occurredAt = diff.occurredAt();
        for (DiscountTarget target : diff.allChangedTargets()) {
            createOutboxIfEligible(target, occurredAt);
        }
    }

    // ---------------------------------------------------------------------------
    // private
    // ---------------------------------------------------------------------------

    private void createOutboxIfEligible(DiscountTarget target, Instant timestamp) {
        if (outboxValidator.canCreateOutbox(target.targetType(), target.targetId())) {
            outboxCommandManager.create(target.targetType(), target.targetId(), timestamp);
        }
    }
}
