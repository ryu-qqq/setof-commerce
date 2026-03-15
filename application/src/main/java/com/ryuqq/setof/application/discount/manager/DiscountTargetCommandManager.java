package com.ryuqq.setof.application.discount.manager;

import com.ryuqq.setof.application.discount.port.out.command.DiscountTargetCommandPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetDiff;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DiscountTargetCommandManager - 할인 적용 대상 저장 매니저.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountTargetCommandManager {

    private final DiscountTargetCommandPort targetCommandPort;

    public DiscountTargetCommandManager(DiscountTargetCommandPort targetCommandPort) {
        this.targetCommandPort = targetCommandPort;
    }

    /**
     * 할인 적용 대상 일괄 저장 (INSERT).
     *
     * @param policyId 소속 정책 ID
     * @param targets 저장할 대상 목록
     */
    public void persistAll(long policyId, List<DiscountTarget> targets) {
        targetCommandPort.persistAll(policyId, targets);
    }

    /**
     * Diff 기반 할인 적용 대상 일괄 persist.
     *
     * <p>added → INSERT, allDirtyTargets(retained + removed) → UPDATE.
     *
     * @param policyId 소속 정책 ID
     * @param diff 타겟 변경 비교 결과
     */
    public void persistDiff(long policyId, DiscountTargetDiff diff) {
        if (!diff.added().isEmpty()) {
            targetCommandPort.persistAll(policyId, diff.added());
        }
        if (!diff.allDirtyTargets().isEmpty()) {
            targetCommandPort.updateAll(policyId, diff.allDirtyTargets());
        }
    }
}
