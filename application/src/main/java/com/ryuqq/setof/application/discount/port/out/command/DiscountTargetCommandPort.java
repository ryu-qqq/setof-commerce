package com.ryuqq.setof.application.discount.port.out.command;

import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import java.util.List;

/**
 * 할인 적용 대상 저장 포트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DiscountTargetCommandPort {

    /**
     * 할인 적용 대상 일괄 저장 (INSERT).
     *
     * @param discountPolicyId 소속 정책 ID
     * @param targets 저장할 대상 목록
     */
    void persistAll(long discountPolicyId, List<DiscountTarget> targets);

    /**
     * 할인 적용 대상 일괄 갱신 (UPDATE).
     *
     * @param discountPolicyId 소속 정책 ID
     * @param targets 갱신할 대상 목록 (비활성화 등)
     */
    void updateAll(long discountPolicyId, List<DiscountTarget> targets);
}
