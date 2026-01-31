package com.ryuqq.setof.domain.refundpolicy.aggregate;

import com.ryuqq.setof.domain.refundpolicy.vo.NonReturnableCondition;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyName;
import java.util.List;

/**
 * 환불 정책 수정 데이터 Value Object.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 */
public record RefundPolicyUpdateData(
        RefundPolicyName policyName,
        int returnPeriodDays,
        int exchangePeriodDays,
        List<NonReturnableCondition> nonReturnableConditions,
        boolean partialRefundEnabled,
        boolean inspectionRequired,
        int inspectionPeriodDays,
        String additionalInfo) {

    public static RefundPolicyUpdateData of(
            RefundPolicyName policyName,
            int returnPeriodDays,
            int exchangePeriodDays,
            List<NonReturnableCondition> nonReturnableConditions,
            boolean partialRefundEnabled,
            boolean inspectionRequired,
            int inspectionPeriodDays,
            String additionalInfo) {
        return new RefundPolicyUpdateData(
                policyName,
                returnPeriodDays,
                exchangePeriodDays,
                nonReturnableConditions,
                partialRefundEnabled,
                inspectionRequired,
                inspectionPeriodDays,
                additionalInfo);
    }
}
